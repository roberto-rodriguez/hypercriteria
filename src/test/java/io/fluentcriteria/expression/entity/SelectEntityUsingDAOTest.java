package io.fluentcriteria.expression.entity;

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
                .leftJoinFetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return userDAO.select().getResultList();
    }

    @Override
    List<User> listDistinctEntities() {
        return userDAO
                .select()
                .distinct()
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .leftJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .innerJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> sample() {
        return userDAO.select()
                //                .distinct()
                //                .leftJoin("address", "a")
                //                .innerJoin("address", "b")
                .innerJoinFetch("address", "ab")
                .getResultList();
    }
}
