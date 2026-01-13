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

    /**
     * Infers the Java type of an attribute referenced by a field path starting
     * from a given root entity class.
     * <p>
     * The field path may reference:
     * <ul>
     * <li>Simple attributes (e.g. {@code "amount"})</li>
     * <li>Nested attributes through {@code @ManyToOne} or {@code @OneToOne}
     * associations (e.g. {@code "user.firstName"})</li>
     * <li>Attributes of collection elements (e.g.
     * {@code "payments.intValue"})</li>
     * </ul>
     *
     * <h3>Join delimiters</h3>
     * The following join delimiters are supported and treated equivalently:
     * <ul>
     * <li>{@code "."} — standard path delimiter</li>
     * <li>{@code ">"} — right join indicator</li>
     * <li>{@code "<>"} — inner join indicator</li>
     * </ul>
     *
     * Join delimiters may appear at the beginning of the path or between path
     * segments. All delimiters are normalized internally before resolution.
     *
     * <pre>{@code
     * inferAttributeType(em, Payment.class, "user.firstName")     → String.class
     * inferAttributeType(em, Payment.class, ">user.firstName")    → String.class
     * inferAttributeType(em, Payment.class, "<>user.payments.id") → Long.class
     * }</pre>
     *
     * <h3>Collection handling</h3>
     * If a path segment refers to a plural attribute (e.g. {@code @OneToMany}),
     * the element type of the collection is used for further path traversal.
     *
     * <h3>Error handling</h3>
     * An {@link IllegalArgumentException} is thrown if:
     * <ul>
     * <li>An attribute name does not exist on the current managed type</li>
     * <li>A path attempts to navigate into a non-managed Java type</li>
     * <li>The field path is syntactically invalid</li>
     * </ul>
     *
     * @param em the {@link EntityManager} used to access the JPA
     * {@link jakarta.persistence.metamodel.Metamodel}
     *
     * @param rootEntityClass the root JPA entity class from which the field
     * path resolution starts
     *
     * @param fieldPath the attribute path to resolve, using
     * {@code "."}, {@code ">"}, or {@code "<>"} as path delimiters
     *
     * @param <A> the inferred attribute type
     *
     * @return the Java {@link Class} representing the type of the final
     * attribute referenced by the field path
     *
     * @throws IllegalArgumentException if the field path references an invalid
     * attribute or navigates into a non-managed type
     */
    @SuppressWarnings("unchecked")
    public static <A> Class<A> inferAttributeType(
            EntityManager em,
            Class<?> rootEntityClass,
            String fieldPath) {

        if (fieldPath.startsWith("<>")) {
            fieldPath = fieldPath.substring(2);
        }

        if (fieldPath.startsWith(">")) {
            fieldPath = fieldPath.substring(1);
        }

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
