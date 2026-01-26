package io.hypercriteria.select.property;

import io.sample.model.Address;
import io.sample.model.State;
import io.sample.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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
    Object selectNestedPropertyOneLevel_inplicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");

        cq.select(addressJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return selectNestedPropertyOneLevel_inplicitJoin(fieldPath);
    }

    @Override
    Object selectNestedPropertyTwoLevels_inplicitJoin(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery(Object.class);

        Root<User> root = cq.from(User.class);
        Join<User, Address> addressJoin = root.join("address");
        Join<Address, State> stateJoin = addressJoin.join("state");

        cq.select(stateJoin.get(lastSegment(fieldPath)));
        return getSingleResult(cq);
    }

    @Override
    Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath) {
        return selectNestedPropertyTwoLevels_inplicitJoin(fieldPath);
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
//
//    @Override
//    public List<String> listByPropertyWithInnerJoin(String fieldPath) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<String> cq = cb.createQuery(String.class);
//
//        Root<User> root = cq.from(User.class);
//        Path<Address> path = root.get("address");
//
//        String[] segments = fieldPath.split("\\.");
//        String lastSegment = segments[segments.length - 1];
//
//        cq.select(path.get(lastSegment));
//
//        return entityManager.createQuery(cq).getResultList();
//    }
//
//    @Override
//    public List<String> listByPropertyWithInnerJoin_withRootAlias(String fieldPath) {
//        return listByPropertyWithInnerJoin(fieldPath);
//    }
//
//    @Override
//    List<String> listDistinctByProperty(String fieldPath) {
//
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<String> cq = cb.createQuery(String.class);
//
//        Root<User> root = cq.from(User.class);
//        Path<String> path = resolveJoinAwarePath(root, fieldPath);
//
//        cq.select(path)
//                .distinct(true)
//                .orderBy(cb.asc(path));
//
//        return entityManager.createQuery(cq).getResultList();
//    }

    private Object getSingleResult(CriteriaQuery cq) {
        return entityManager.createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
