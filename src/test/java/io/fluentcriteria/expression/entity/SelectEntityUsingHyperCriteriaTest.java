package io.fluentcriteria.expression.entity;

import io.fluentcriteria.FluentCriteria;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingHyperCriteriaTest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getSingleResult();
    }

    @Override
    Object selectNestedEntity(Class rootType, String path) {
        return FluentCriteria.using(entityManager)
                .select(path)
                .from(rootType)
                .getSingleResult();
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getResultList();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .leftJoinFetch(fetchPath)
                .getResultList();
    }

}
