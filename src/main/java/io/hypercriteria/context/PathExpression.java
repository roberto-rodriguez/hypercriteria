package io.hypercriteria.context;

import static io.hypercriteria.util.TypeUtil.getAssociationJavaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;

/**
 *
 * @author rrodriguez
 */
@AllArgsConstructor
public final class PathExpression {

    /**
     * Can be: - Full field path, having implicit joins. Ex: "p.user.name" -
     * Alias.field, referencing to explicitly defined joins. Ex: "u.name"
     */
    private final String rawPath;
    private final String[] segments;

    //In case the pathExpression is used from multiple contexts.
    private final Map<Class<?>, PathMetadata> metadataByRoot = new HashMap<>();

    Function<Class<?>, Class<?>> returnTypeResolver;

    public PathExpression(String rawPath) {
        this.rawPath = rawPath;
        this.segments = rawPath.split("\\.");
    }

    public PathExpression(String rawPath, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.rawPath = rawPath;
        this.segments = rawPath.split("\\.");
        this.returnTypeResolver = returnTypeResolver;
    }

    @Override
    public String toString() {
        return rawPath;
    }

    public int joinSegmentCount(QueryContext ctx) {
        return getTerminal(ctx).isPresent()
                ? segments.length - 1
                : segments.length;
    }

    public Optional<String> getTerminal(QueryContext ctx) {
        return getPathMetadata(ctx).getTerminal();
    }

    public Class<?> getJavaType(QueryContext ctx) {
        return getPathMetadata(ctx).getJavaType();
    }

    public Class<?> getReturnType(QueryContext ctx) {
        return getPathMetadata(ctx).getReturnType();
    }

    public boolean getIsAssociation(QueryContext ctx) {
        return getPathMetadata(ctx).isAssociation();
    }

    //Resolve PathMetadata lazily, at the time one of its properties are requested.
    //This is because at the time the PathExpression is created we don't have ctx yet.
    private PathMetadata getPathMetadata(QueryContext ctx) {
        Class<?> rootType = ctx.getRootType();
        return metadataByRoot.computeIfAbsent(
                rootType,
                rt -> resolvePathMetadata(ctx)
        );
    }

    private PathMetadata resolvePathMetadata(QueryContext ctx) {
        Metamodel mm = ctx.getEntityManager().getMetamodel();

        // Special case: empty path ("")
        if (segments.length == 1 && segments[0].isEmpty()) {
            return new PathMetadata(
                    Optional.empty(),
                    ctx.getRootType(),
                    false, //isAssociation
                    returnTypeResolver
            );
        }

        String firstSegment = segments[0];

        int index;
        ManagedType<?> type;

        // 1️⃣ Resolve starting type via aliasTypeMap
        Class<?> startJavaType = ctx.getAliasTypeMap().get(firstSegment);
        if (startJavaType != null) {
            type = mm.managedType(startJavaType);
            index = 1;
        } else {
            type = mm.managedType(ctx.getRootType());
            index = 0;
        }

        // 2️⃣ Walk segments
        for (int i = index; i < segments.length; i++) {
            String segment = segments[i];
            Attribute<?, ?> attr = type.getAttribute(segment);
            boolean last = (i == segments.length - 1);

            Attribute.PersistentAttributeType pat
                    = attr.getPersistentAttributeType();

            boolean isAssociation
                    = pat == Attribute.PersistentAttributeType.ONE_TO_ONE
                    || pat == Attribute.PersistentAttributeType.MANY_TO_ONE
                    || pat == Attribute.PersistentAttributeType.ONE_TO_MANY
                    || pat == Attribute.PersistentAttributeType.MANY_TO_MANY;

            if (last) {
                Class<?> javaType = isAssociation
                        ? getAssociationJavaType(attr)
                        : attr.getJavaType();

                return new PathMetadata(
                        isAssociation ? Optional.empty() : Optional.of(segment),
                        javaType,
                        isAssociation,
                        returnTypeResolver
                );
            }

            // Navigate to next type
            if (pat == Attribute.PersistentAttributeType.EMBEDDED) {
                type = mm.embeddable(attr.getJavaType());
            } else if (isAssociation) {
                type = mm.managedType(getAssociationJavaType(attr));
            } else {
                throw new IllegalArgumentException(
                        "Cannot navigate through non-association attribute '"
                        + segment + "' on type '"
                        + type.getJavaType().getName() + "'"
                );
            }
        }

        throw new IllegalStateException("Unreachable");
    }

    public String[] getSegments() {
        return segments;
    }

    public String getRawPath() {
        return rawPath;
    }

    public Function<Class<?>, Class<?>> getReturnTypeResolver() {
        return returnTypeResolver;
    }

}
