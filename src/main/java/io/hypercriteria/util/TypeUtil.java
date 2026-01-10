package io.hypercriteria.util;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;

/**
 *
 * @author rrodriguez
 */
public class TypeUtil {

    @SuppressWarnings("unchecked")
    public static <A> Class<A> inferAttributeType(
            EntityManager em,
            Class<?> rootEntityClass,
            String fieldPath) {

        // Normalize join delimiters: ".", ">", "<>" → "."
        String normalizedPath = fieldPath
                .replace("<>", ".")
                .replace(">", ".");

        Metamodel metamodel = em.getMetamodel();
        ManagedType<?> currentType = metamodel.managedType(rootEntityClass);

        String[] parts = normalizedPath.split("\\.");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            Attribute<?, ?> attribute;
            try {
                attribute = currentType.getAttribute(part);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid attribute '" + part
                        + "' in path '" + fieldPath
                        + "' for type " + currentType.getJavaType().getSimpleName(),
                        ex
                );
            }

            boolean isLast = (i == parts.length - 1);

            if (isLast) {
                return (Class<A>) attribute.getJavaType();
            }

            Class<?> javaType;

            if (attribute.isCollection()) {
                PluralAttribute<?, ?, ?> plural
                        = (PluralAttribute<?, ?, ?>) attribute;
                javaType = plural.getElementType().getJavaType();
            } else {
                javaType = attribute.getJavaType();
            }

            try {
                currentType = metamodel.managedType(javaType);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Attribute '" + part
                        + "' in path '" + fieldPath
                        + "' does not refer to a managed type",
                        ex
                );
            }
        }

        throw new IllegalArgumentException("Invalid field path: " + fieldPath);
    }
}
