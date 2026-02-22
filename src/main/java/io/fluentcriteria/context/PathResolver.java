package io.fluentcriteria.context;

import java.util.Map;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.JoinType;

public final class PathResolver {

    public static JoinNode resolveJoinPath(
            QueryContext ctx,
            String joinPath,
            JoinType joinType,
            boolean processingFetch,
            boolean declaredExplicitly,
            String explicitAlias,
            boolean hasOnPredicate
    ) {
        if (joinPath.isBlank()) {
            // Special case when creating root with alias. Example .from(User.class, "u")
            return ctx.getRootNode();
        }

        String[] segments = joinPath.split("\\.");
        String firstSegment = segments[0];

        JoinNode current;
        int index;

        // 1️⃣ Determine starting JoinNode via aliasTypeMap
        if (ctx.getAliasTypeMap().containsKey(firstSegment)) {

            current = ctx.getAliases().get(firstSegment);

            if (current == null) {
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
            boolean isLast = (i == segments.length - 1);

            // ✅ Terminal-only explicitness:
            // Only the LAST segment uses declaredExplicitly + explicitAlias.
            boolean segmentExplicit = declaredExplicitly && isLast;
            String segmentAlias = segmentExplicit ? explicitAlias : null;
            // ON applies only to last segment, and only for non-fetch
            boolean segmentHasOn = segmentExplicit && isLast && hasOnPredicate && !processingFetch;

            if (processingFetch) {
                current = resolveFetch(ctx, current, segment, joinType, segmentExplicit, segmentAlias);
            } else {
                current = resolveJoin(ctx, current, segment, joinType, segmentExplicit, segmentAlias, segmentHasOn);
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
            String explicitAlias,
            boolean hasOnPredicate
    ) {
        // If implicit, try to reuse explicit join, but ONLY if it has NO ON
        if (!declaredExplicitly) {
            JoinNode reusable = findReusableJoin(ctx, parent, field, joinType);
            if (reusable != null) {
                return reusable;
            }
        }

        JoinKey key = new JoinKey(
                parent,
                field,
                joinType,
                declaredExplicitly ? explicitAlias : null,
                hasOnPredicate
        );

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
        // We intentionally do NOT reuse fetch nodes for implicit joins:
        // implicit path traversal should not turn into fetching.
        FetchParent<?, ?> parentFetch = (FetchParent<?, ?>) parent.getFrom();
        Fetch<?, ?> fetch = joinType == null ? parentFetch.fetch(field) : parentFetch.fetch(field, joinType);

        JoinKey key = new JoinKey(parent, field, joinType, declaredExplicitly ? explicitAlias : null, /*hasOnPredicate = */ false);
        return ctx.getJoins().computeIfAbsent(key, k -> new FetchNode(k, fetch));
    }

    /**
     * Resolves a path expression like "p.user.name" and returns the JoinNode of
     * the LAST JOIN in the path.
     */
    public static JoinNode resolvePath(
            QueryContext ctx,
            PathExpression path
    ) {
        String[] segments = path.getSegments();
        JoinNode current;
        int index;

        if (ctx.getAliases().containsKey(segments[0])) {
            current = ctx.getAliases().get(segments[0]);
            index = 1;
        } else {
            current = ctx.getRootNode();
            index = 0;
        }

        int end = path.getIsAssociation(ctx) ? segments.length : segments.length - 1;

        for (int i = index; i < end; i++) {
            // implicit join resolution: declaredExplicitly=false, explicitAlias=null
            current = resolveJoin(ctx, current, segments[i], JoinType.LEFT, false, null, /*hasOnPredicate = */ false);
        }

        return current;
    }

    /**
     * For implicit joins: try to reuse an existing explicit join (unambiguous),
     * ignoring explicitAlias.
     *
     * Returns: - JoinNode if exactly one match exists - null if none exists -
     * null if ambiguous (more than one match)
     */
    private static JoinNode findReusableJoin(QueryContext ctx, JoinNode parent, String field, javax.persistence.criteria.JoinType joinType) {
        JoinNode found = null;

        for (Map.Entry<JoinKey, JoinNode> e : ctx.getJoins().entrySet()) {
            JoinKey k = e.getKey();

            if (k.parent == parent
                    && (k.field == null ? field == null : k.field.equals(field))
                    && k.joinType == joinType
                    && !k.hasOnPredicate) {

                if (found != null) {
                    // Ambiguous: multiple joins match (usually different explicitAlias)
                    return null;
                }
                found = e.getValue();
            }
        }

        return found;
    }
}
