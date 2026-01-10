package io.hypercriteria.select.entity;

import io.hypercriteria.HyperCriteria;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingHyperCriteriaTest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        return HyperCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getSingleResult();
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        return HyperCriteria.using(entityManager)
                .select()
                .from(User.class)
                .fetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return HyperCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getResultList();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        return HyperCriteria.using(entityManager)
                .select()
                .from(User.class)
                .fetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        return HyperCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .fetch(fetchPath)
                .getResultList();
    }

}
