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
                .fetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return userDAO.select().getResultList();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .fetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .fetch(fetchPath)
                .getResultList();
    }
}
