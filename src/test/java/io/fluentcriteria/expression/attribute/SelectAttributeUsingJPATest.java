package io.fluentcriteria.expression.attribute;

import io.sample.model.Address;
import io.sample.model.State;
import io.sample.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author rrodriguez
 */
class SelectAttributeUsingJPATest extends BaseSelectAttributeTest {

    @Override
    Object selectAttribute(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Path<?> path = root.get(lastSegment(fieldPath));

        cq.select(path);
        return getSingleResult(cq);
    }

    @Override
    Object selectAttribute_withRootAlias(String fieldPath) {
        return selectAttribute(fieldPath);
    }

    @Override
    Object selectNestedAttributeOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        String[] segments = fieldPath.split("\\.");

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join(segments[0]);

        cq.select(addressJoin.get(segments[1]));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedAttributeOneLevel_explicitLeftJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");

        cq.select(addressJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedAttributeTwoLevels_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");
        Join<Address, State> stateJoin = addressJoin.join("state");

        cq.select(stateJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    private Object getSingleResult(CriteriaQuery cq) {
        return entityManager.createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    Object selectNestedAttributeTwoLevels_explicitLeftJoin(String fieldPath) {
        return selectNestedAttributeTwoLevels_implicitJoin(fieldPath);
    }

    private String lastSegment(String fieldPath) {
        String[] segments = fieldPath.split("\\.");
        return segments[segments.length - 1];
    }

    @Override
    List<String> listAttribute(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listAttribute_distinct(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedAttributeOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedAttributeOneLevel_explicitLeftJoin(String fieldPath) {
        return listNestedAttributeOneLevel_implicitJoin(fieldPath);
    }

    @Override
    List<String> listNestedAttributeOneLevel_explicitInnerJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.INNER);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Join<Address, State> stateJoin = addressJoin.join("state", JoinType.LEFT);
        Path<String> path = stateJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> testListNestedAttributeTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return listNestedAttributeTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedAttributeTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins_distinct(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Join<Address, State> stateJoin = addressJoin.join("state", JoinType.LEFT);
        Path<String> path = stateJoin.get(lastSegment(fieldPath));

        cq.select(path).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedAttributeTwoLevels_explicitLeftJoins(String fieldPath) {
        return listNestedAttributeTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedAttributeTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Join<Address, State> stateJoin = addressJoin.join("state", JoinType.INNER);
        Path<String> path = stateJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }
}
