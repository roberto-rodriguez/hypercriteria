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
    Object selectEntityWithFetchPath(String fetchPath) {
        return userDAO
                .select()
                .fetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return userDAO.select().list();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .fetch(fetchPath)
                .list();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .fetch(fetchPath)
                .list();
    }
}
