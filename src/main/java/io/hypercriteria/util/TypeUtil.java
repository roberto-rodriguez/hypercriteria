package io.hypercriteria.util;

/**
 *
 * @author rrodriguez
 */
import io.hypercriteria.Criteria;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;

public class TypeUtil {

    public static Class<?> resolveJavaType(String joinPath, Criteria criteria) {

        if (joinPath == null || joinPath.isBlank()) {
            throw new IllegalArgumentException("joinPath must not be null or empty");
        }

        Metamodel mm = criteria.getEntityManager().getMetamodel();

        // Normalize delimiters
        String normalizedPath = joinPath.replace(">", ".").replace("<>", ".");
        String[] segments = normalizedPath.split("\\.");

        String firstSegment = segments[0];

        ManagedType<?> type;
        int index;

        // 1️⃣ Resolve starting type via aliasTypeMap (root alias included as "")
        Class<?> startType = criteria.getAliasTypeMap().get(firstSegment);
        if (startType != null) {
            type = mm.managedType(startType);
            index = 1;
        } else {
            type = mm.managedType(criteria.getEntityType());
            index = 0;
        }

        // 2️⃣ Walk path segments
        for (int i = index; i < segments.length; i++) {
            String segment = segments[i];

            Attribute<?, ?> attr;
            try {
                attr = type.getAttribute(segment);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Could not resolve attribute '" + segment
                        + "' on type '" + type.getJavaType().getName() + "'",
                        ex
                );
            }

            if (!attr.isAssociation()) {
                throw new IllegalArgumentException(
                        "Attribute '" + segment
                        + "' on type '" + type.getJavaType().getName()
                        + "' is not an association"
                );
            }

            // Always navigate to the association Java type
            Class<?> associationType = getAssociationJavaType(attr);
            type = mm.managedType(associationType);
        }

        return type.getJavaType();
    }

    /**
     * Returns the Java type of an association. - Singular association →
     * attribute Java type - Plural association → element Java type
     *
     * @param attr
     * @return
     */
    public static Class<?> getAssociationJavaType(Attribute<?, ?> attr) {

        if (attr instanceof PluralAttribute<?, ?, ?> plural) {
            return plural.getElementType().getJavaType();
        }

        return attr.getJavaType();
    }
}
