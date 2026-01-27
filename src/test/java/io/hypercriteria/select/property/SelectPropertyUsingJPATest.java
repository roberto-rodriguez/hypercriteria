package io.hypercriteria.select.property;

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
class SelectPropertyUsingJPATest extends BaseSelectPropertyTest {

    @Override
    Object selectProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Path<?> path = root.get(fieldPath);

        cq.select(path);
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedPropertyOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");

        cq.select(addressJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return selectNestedPropertyOneLevel_implicitJoin(fieldPath);
    }

    @Override
    Object selectNestedPropertyTwoLevels_implicitJoin(String fieldPath) {
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
    Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath) {
        return selectNestedPropertyTwoLevels_implicitJoin(fieldPath);
    }

    private String lastSegment(String fieldPath) {
        String[] segments = fieldPath.split("\\.");
        return segments[segments.length - 1];
    }

    @Override
    List<String> listProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listProperty_distinct(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Path<String> path = root.get(lastSegment(fieldPath));

        cq.select(path).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedPropertyOneLevel_implicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.LEFT);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return listNestedPropertyOneLevel_implicitJoin(fieldPath);
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitInnerJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address", JoinType.INNER);
        Path<String> path = addressJoin.get(lastSegment(fieldPath));

        cq.select(path);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins(String fieldPath) {
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
    List<String> testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return listNestedPropertyTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedPropertyTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_distinct(String fieldPath) {
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
    List<String> listNestedPropertyTwoLevels_explicitLeftJoins(String fieldPath) {
        return listNestedPropertyTwoLevels_implicitJoins(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
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
