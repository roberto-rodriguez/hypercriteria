package io.fluentcriteria.context;

import java.util.Arrays;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.JoinType;

/**
 *
 * @author rrodriguez
 */
public final class PathResolver {

    public static JoinNode resolveJoinPath(
            QueryContext ctx,
            String joinPath,
            JoinType joinType,
            boolean processingFetch,
            boolean declaredExplicitly,
            String explicitAlias
    ) {
        if (joinPath.isBlank()) {
            System.out.println("joinPath.isBlank(), return  ctx.getRootNode()");
            //Special case when creating root with alias. Example .from(User.class, "u")
            return ctx.getRootNode();
        }

        String[] segments = joinPath.split("\\.");
        String firstSegment = segments[0];

        System.out.println("PathResolver.resolveJoinPath :: joinPath = " + joinPath);
        System.out.println("PathResolver.resolveJoinPath :: firstSegment = " + firstSegment);
        System.out.println("PathResolver.resolveJoinPath :: segments = " + segments);

        JoinNode current;
        int index;

        // 1️⃣ Determine starting JoinNode via aliasTypeMap
        if (ctx.getAliasTypeMap().containsKey(firstSegment)) {
            // Alias may or may not already have a JoinNode
            current = ctx.getAliases().get(firstSegment);

            if (current == null) {
                // Root alias case ("")
                if (firstSegment.equals(ctx.getRootAlias())) {
                    current = ctx.getRootNode();
                } else {
                    throw new IllegalStateException(
                            "Alias '" + firstSegment + "' has no JoinNode"
                    );
                }
            }

            index = 1;
        } else {
            // Implicit root
            current = ctx.getRootNode();
            index = 0;
        }

        // 2️⃣ Walk remaining segments
        for (int i = index; i < segments.length; i++) {
            String segment = segments[i];

            if (processingFetch) {
                // keep fetch in same joins registry; allow alias if you want
                current = resolveFetch(ctx, current, segment, joinType, declaredExplicitly, explicitAlias);
            } else {
                current = resolveJoin(ctx, current, segment, joinType, declaredExplicitly, explicitAlias);
            }
        }

        return current;
    }

    private static JoinNode resolveJoin(
            QueryContext ctx,
            JoinNode parent,
            String field,
            JoinType joinType,
            boolean declaredExplicitly,
            String explicitAlias
    ) {
        JoinKey key = new JoinKey(parent, field, joinType, declaredExplicitly ? explicitAlias : null);
        return ctx.getJoins().computeIfAbsent(key, k -> new JoinNode(k, declaredExplicitly));
    }

    private static JoinNode resolveFetch(
            QueryContext ctx,
            JoinNode parent,
            String field,
            JoinType joinType,
            boolean declaredExplicitly,
            String explicitAlias
    ) {
        FetchParent<?, ?> parentFetch = (FetchParent<?, ?>) parent.getFrom();
        Fetch<?, ?> fetch = joinType == null ? parentFetch.fetch(field) : parentFetch.fetch(field, joinType);

        JoinKey key = new JoinKey(parent, field, joinType, declaredExplicitly ? explicitAlias : null);
        return ctx.getJoins().computeIfAbsent(key, k -> new FetchNode(k, fetch));
    }

    /**
     * Resolves a path expression like "p.user.name" and returns the JoinNode of
     * the LAST JOIN in the path.
     *
     * @param ctx
     * @param path
     * @return
     */
    public static JoinNode resolvePath(
            QueryContext ctx,
            PathExpression path
    ) {
        String[] segments = path.getSegments();

        System.out.println("DEBUG :: PathResolver.resolvePath  - segments = " + Arrays.toString(segments));

        JoinNode current;
        int index;

        if (ctx.getAliases().containsKey(segments[0])) {
            System.out.println("DEBUG :: PathResolver.resolvePath  - ctx.getAliases().containsKey(segments[0])");
            current = ctx.getAliases().get(segments[0]);
            index = 1;
        } else {
            current = ctx.getRootNode();
            index = 0;
        }

        System.out.println(String.format("DEBUG :: PathResolver.resolvePath  - current = %s, index = %s", current, index));

        //If is not an association, the last segment is the attributeName, so no need to create join for it
        int end = path.getIsAssociation(ctx) ? segments.length : segments.length - 1;

        // Stop BEFORE the terminal field
        for (int i = index; i < end; i++) {
            current = resolveJoin(ctx, current, segments[i], JoinType.LEFT, false, null);
            System.out.println(String.format("DEBUG :: PathResolver.resolvePath  - calling %s - resolveJoin(%s)  = %s", i, segments[i], current));
        }

        return current;
    }

}
