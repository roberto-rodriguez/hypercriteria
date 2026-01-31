package io.hypercriteria.select.entity;

import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingDAOTest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        return userDAO.select().getSingleResult();
    }

    @Override
    Object selectNestedEntity(Class rootType, String path) {
        return userDAO.select(path)
                .from(rootType)
                .getSingleResult();
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        return userDAO
                .select()
                .fetchLeft(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return userDAO.select().getResultList();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .fetchLeft(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .fetchLeft(fetchPath)
                .getResultList();
    }
    
//       @Override
//    List<User> sample() {
//            return userDAO.select()
//                    .fetchLeft("address", "a")
//                    .where(eq("a.zipcode"), "123456")
//                    .get;
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//        Root<User> root = cq.distinct(true).from(User.class);
//
//        Fetch<User, Address> addressFetch = root.fetch("address", JoinType.LEFT);
//
//        Join<User, Address> addressJoin
//                = (Join<User, Address>) addressFetch;
//
//        cq.where(
//                cb.like(addressJoin.get("zipcode"), "123456")
//        );
//
//        return entityManager.createQuery(cq).getResultList();
//    }
}
