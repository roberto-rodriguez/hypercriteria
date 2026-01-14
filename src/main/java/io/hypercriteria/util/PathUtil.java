package io.hypercriteria.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import static javax.persistence.criteria.JoinType.INNER;
import static javax.persistence.criteria.JoinType.LEFT;
import static javax.persistence.criteria.JoinType.RIGHT;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;

/**
 *
 * @author rrodriguez
 */
public class PathUtil {

    /**
     * Resolves a join-aware field path into structural metadata describing:
     * <ul>
     * <li>the Java type of the terminal segment</li>
     * <li>whether the path ends in an association</li>
     * <li>the effective join and attribute components</li>
     * <li>the required joins and their join types</li>
     * </ul>
     *
     * <p>
     * This method parses a field path that may include explicit join delimiters
     * and navigates the JPA Metamodel starting from the given root entity
     * class. While traversing the path, it registers all required joins into
     * the supplied {@code fieldsMappingAlias} map, applying join type
     * precedence rules.
     * </p>
     *
     * <h3>Supported join delimiters</h3>
     *
     * <ul>
     * <li><b>.</b> — LEFT join (default)</li>
     * <li><b>&gt;</b> — RIGHT join</li>
     * <li><b>&lt;&gt;</b> — INNER join</li>
     * </ul>
     *
     * <p>
     * A join delimiter may appear at the beginning of the path to explicitly
     * define the join type of the root association.
     * </p>
     *
     * <h3>Association vs attribute resolution</h3>
     *
     * <p>
     * The returned {@link PathInfo#isEndsInAssociation() endsInAssociation}
     * flag indicates whether the <em>last segment</em> of the path refers to an
     * association (singular or collection) rather than a basic attribute.
     * </p>
     *
     * <p>
     * This distinction is critical for query construction, as operations such
     * as {@code count}, {@code exists}, or join-based predicates must behave
     * differently when the path terminates in an association.
     * </p>
     *
     * <h3>Join registration rules</h3>
     *
     * <ul>
     * <li>If the path consists of a single segment and ends in an association,
     * that segment is registered as a join.</li>
     * <li>If the path contains multiple segments, all intermediate segments are
     * registered as joins.</li>
     * <li>If the path ends in an association, the terminal segment is also
     * registered as a join.</li>
     * <li>If the path ends in an attribute, the terminal segment is never
     * registered as a join.</li>
     * </ul>
     *
     * <h3>Join precedence</h3>
     *
     * <p>
     * When the same join path is registered multiple times, the join with the
     * highest precedence is retained:
     * </p>
     *
     * <pre>{@code
     * INNER &gt; RIGHT &gt; LEFT
     * }</pre>
     *
     * <h3>Derived path components</h3>
     *
     * <ul>
     * <li>{@link PathInfo#getLastJoin()} represents the final join alias that
     * should be used when constructing Criteria expressions.</li>
     * <li>{@link PathInfo#getAttributeName()} contains the terminal attribute
     * name if the path ends in an attribute; otherwise it is empty.</li>
     * </ul>
     *
     * <h3>Examples</h3>
     *
     * <pre>{@code
     * getPathInfo(em, User.class, "firstName", map)
     *   → endsInAssociation = false
     *   → attributeName = "firstName"
     *   → lastJoin = ""
     *
     * getPathInfo(em, User.class, "address.city", map)
     *   → endsInAssociation = false
     *   → attributeName = "city"
     *   → lastJoin = "address"
     *
     * getPathInfo(em, User.class, "payments", map)
     *   → endsInAssociation = true
     *   → attributeName = empty
     *   → lastJoin = "payments"
     *
     * getPathInfo(em, User.class, "<>payments.user", map)
     *   → endsInAssociation = true
     *   → attributeName = empty
     *   → lastJoin = "user"
     * }</pre>
     *
     * @param em the {@link EntityManager} used to access the JPA Metamodel
     * @param rootEntityClass the root entity type from which the field path is
     * resolved
     * @param fieldPath a join-aware field path using {@code .}, {@code >}, or
     * {@code <>} as join delimiters
     * @param fieldsMappingAlias a map populated with join paths and their
     * corresponding alias and {@link JoinType}; existing entries may be
     * overridden based on join precedence
     *
     * @return a {@link PathInfo} describing the resolved field path
     *
     * @throws IllegalArgumentException if {@code fieldPath} is null, blank,
     * refers to a non-existent attribute, or attempts to traverse a non-managed
     * type
     */

    public static PathInfo getPathInfo(
            EntityManager em,
            Class<?> rootEntityClass,
            String fieldPath,
            LinkedHashMap<String, AliasJoinType> fieldsMappingAlias) {

        if (fieldPath == null) {
            throw new IllegalArgumentException("Field path must not be null");
        }

        if (fieldPath.trim().isEmpty()) {//Path info is the root (used in count() and countDistinct())
            return PathInfo.builder()
                    .javaType(rootEntityClass)
                    .lastJoin("")
                    .attributeName(Optional.empty())
                    .build();
        }

        /* -------------------------------------------------
     * 1. Parse join-aware segments and join types
     * ------------------------------------------------- */
        List<String> segments = new ArrayList<>();
        List<JoinType> joinTypes = new ArrayList<>();

        int i = 0;
        JoinType rootJoinType = JoinType.LEFT;

        if (fieldPath.startsWith("<>")) {
            rootJoinType = JoinType.INNER;
            i = 2;
        } else if (fieldPath.startsWith(">")) {
            rootJoinType = JoinType.RIGHT;
            i = 1;
        }

        StringBuilder current = new StringBuilder();

        while (i < fieldPath.length()) {
            if (fieldPath.startsWith("<>", i)) {
                segments.add(current.toString());
                joinTypes.add(JoinType.INNER);
                current.setLength(0);
                i += 2;
            } else if (fieldPath.charAt(i) == '>') {
                segments.add(current.toString());
                joinTypes.add(JoinType.RIGHT);
                current.setLength(0);
                i++;
            } else if (fieldPath.charAt(i) == '.') {
                segments.add(current.toString());
                joinTypes.add(JoinType.LEFT);
                current.setLength(0);
                i++;
            } else {
                current.append(fieldPath.charAt(i));
                i++;
            }
        }

        segments.add(current.toString());

        int len = segments.size();

        /* -------------------------------------------------
     * 2. Infer type + endsInAssociation via Metamodel
     * ------------------------------------------------- */
        Metamodel metamodel = em.getMetamodel();
        ManagedType<?> currentType = metamodel.managedType(rootEntityClass);

        Attribute<?, ?> lastAttribute = null;

        for (int idx = 0; idx < len; idx++) {
            String segment = segments.get(idx);

            Attribute<?, ?> attribute;
            try {
                attribute = currentType.getAttribute(segment);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid attribute '" + segment
                        + "' in path '" + fieldPath
                        + "' for type " + currentType.getJavaType().getSimpleName(),
                        ex
                );
            }

            lastAttribute = attribute;

            boolean isLast = (idx == len - 1);
            if (isLast) {
                break;
            }

            Class<?> nextType;
            if (attribute.isCollection()) {
                PluralAttribute<?, ?, ?> plural = (PluralAttribute<?, ?, ?>) attribute;
                nextType = plural.getElementType().getJavaType();
            } else {
                nextType = attribute.getJavaType();
            }

            try {
                currentType = metamodel.managedType(nextType);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Attribute '" + segment
                        + "' in path '" + fieldPath
                        + "' does not refer to a managed type",
                        ex
                );
            }
        }

        boolean endsInAssociation = lastAttribute != null && lastAttribute.isAssociation();
        Class<?> javaType = lastAttribute == null ? null : lastAttribute.getJavaType();

        /* -------------------------------------------------
     * 3. Register joins (now aware of endsInAssociation)
     * ------------------------------------------------- */
        if ((len == 1 && endsInAssociation) //In this case the fieldPath is the join
                || len > 1) {
            // Root mapping
            putWithPrecedence(
                    fieldsMappingAlias,
                    segments.get(0),
                    segments.get(0),
                    rootJoinType
            );

            int joinLimit = endsInAssociation ? len - 1 : len - 2;

            for (int idx = 0; idx < joinLimit; idx++) {
                String key = segments.get(idx) + "." + segments.get(idx + 1);
                putWithPrecedence(
                        fieldsMappingAlias,
                        key,
                        segments.get(idx + 1),
                        joinTypes.get(idx)
                );
            }
        }

        /* -------------------------------------------------
     * 4. Derive lastJoin and attributeName
     * ------------------------------------------------- */
        String lastJoin = "";
        String attributeName;

        if (len == 1) {
            if (endsInAssociation) {
                lastJoin = segments.get(0);
                attributeName = null;
            } else {

                attributeName = segments.get(0);
            }
        } else {
            if (endsInAssociation) {
                lastJoin = segments.get(len - 1);
                attributeName = null;
            } else {
                lastJoin = segments.get(len - 2);
                attributeName = segments.get(len - 1);
            }
        }

        /* -------------------------------------------------
     * 5. Build result DTO
     * ------------------------------------------------- */
        return PathInfo.builder()
                .javaType(javaType)
                .endsInAssociation(endsInAssociation)
                .lastJoin(lastJoin)
                .attributeName(Optional.ofNullable(attributeName))
                .build();
    }

    private static void putWithPrecedence(
            Map<String, AliasJoinType> map,
            String key,
            String alias,
            JoinType newJoinType) {

        AliasJoinType existing = map.get(key);

        if (existing == null || precedence(newJoinType) > precedence(existing.getJoinType())) {
            map.put(
                    key,
                    AliasJoinType.builder()
                            .alias(alias)
                            .joinType(newJoinType)
                            .build()
            );
        }
    }

    private static int precedence(JoinType joinType) {
        return switch (joinType) {
            case INNER ->
                3;
            case RIGHT ->
                2;
            case LEFT ->
                1;
            default ->
                0;
        };
    }

    //Used for testing
    public static Class<?> getAttributeType(
            EntityManager em,
            Class<?> rootEntityClass,
            String fieldPath) {
        PathInfo pathInfo = getPathInfo(em, rootEntityClass, fieldPath, new LinkedHashMap());
        return pathInfo.getJavaType();
    }

}
