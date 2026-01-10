 
package io.hypercriteria.select.property; 

import io.sample.model.User;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author rrodriguez
 */
class SelectPropertyUsingJPATest extends BaseSelectPropertyTest {

    @Override
    Object selectByProperty(String propertyName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Path<?> path = resolveJoinAwarePath(root, propertyName);

        cq.select(path);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    List<String> listByProperty(String propertyName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = resolveJoinAwarePath(root, propertyName);

        cq.select(path);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listDistinctByProperty(String propertyName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = resolveJoinAwarePath(root, propertyName);

        cq.select(path)
          .distinct(true)
          .orderBy(cb.asc(path));

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * Resolves a join-aware field path.
     *
     * Supported delimiters:
     *   .   -> LEFT join (default)
     *   >   -> RIGHT join
     *   <>  -> INNER join
     *
     * Examples:
     *   firstName
     *   address.street
     *   address<>state.name
     *   >address.city
     */
    @SuppressWarnings("unchecked")
    private <T> Path<T> resolveJoinAwarePath(Root<User> root, String fieldPath) {

        JoinType currentJoinType = JoinType.LEFT; // default
        String path = fieldPath;

        // Root join override
        if (path.startsWith("<>")) {
            currentJoinType = JoinType.INNER;
            path = path.substring(2);
        } else if (path.startsWith(">")) {
            currentJoinType = JoinType.RIGHT;
            path = path.substring(1);
        }

        From<?, ?> from = root;

        while (true) {

            int nextDelimiterIndex = findNextDelimiter(path);
            if (nextDelimiterIndex == -1) {
                return (Path<T>) from.get(path);
            }

            String attribute = path.substring(0, nextDelimiterIndex);
            String delimiter = extractDelimiter(path, nextDelimiterIndex);

            from = from.join(attribute, currentJoinType);

            path = path.substring(nextDelimiterIndex + delimiter.length());
            currentJoinType = toJoinType(delimiter);
        }
    }

    private int findNextDelimiter(String path) {
        int dot = path.indexOf('.');
        int right = path.indexOf('>');
        int inner = path.indexOf("<>");

        int idx = Integer.MAX_VALUE;

        if (dot != -1) idx = dot;
        if (right != -1) idx = Math.min(idx, right);
        if (inner != -1) idx = Math.min(idx, inner);

        return idx == Integer.MAX_VALUE ? -1 : idx;
    }

    private String extractDelimiter(String path, int index) {
        if (path.startsWith("<>", index)) {
            return "<>";
        }
        return String.valueOf(path.charAt(index));
    }

    private JoinType toJoinType(String delimiter) {
        return switch (delimiter) {
            case "." -> JoinType.LEFT;
            case ">" -> JoinType.RIGHT;
            case "<>" -> JoinType.INNER;
            default -> throw new IllegalArgumentException("Unsupported join delimiter: " + delimiter);
        };
    }
}
