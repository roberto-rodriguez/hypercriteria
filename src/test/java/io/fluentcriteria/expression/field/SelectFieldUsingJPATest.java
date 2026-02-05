package io.fluentcriteria.expression.field;

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
class SelectFieldUsingJPATest extends BaseSelectFieldTest {

    @Override
    Object selectField(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Path<?> path = root.get(lastSegment(fieldPath));

        cq.select(path);
        return getSingleResult(cq);
    }

    @Override
    Object selectField_withRootAlias(String fieldPath) {
        return selectField(fieldPath);
    }

    @Override
    Object selectNestedFieldOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        String[] segments = fieldPath.split("\\.");

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join(segments[0]);

        cq.select(addressJoin.get(segments[1]));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedFieldOneLevel_explicitLeftJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");

        cq.select(addressJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedFieldTwoLevels_implicitJoin(String fieldPath) {
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
    Object selectNestedFieldTwoLevels_explicitLeftJoin(String fieldPath) {
        return selectNestedFieldTwoLevels_implicitJoin(fieldPath);
    }

    private String lastSegment(String fieldPath) {
        String[] segments = fieldPath.split("\\.");
        return segments[segments.length - 1];
    }

    @Override
    List<String> listField(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listField_distinct(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedFieldOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitLeftJoin(String fieldPath) {
        return listNestedFieldOneLevel_implicitJoin(fieldPath);
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitInnerJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.INNER);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins(String fieldPath) {
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
    List<String> testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return listNestedFieldTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedFieldTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_distinct(String fieldPath) {
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
    List<String> listNestedFieldTwoLevels_explicitLeftJoins(String fieldPath) {
        return listNestedFieldTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
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
