package io.hypercriteria.select.entity;

import io.sample.model.User;
import io.utility.TypeUtil;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingJPATest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class);

        return entityManager
                .createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    Object selectNestedEntity(Class rootType, String fieldPath) {
        Class<?> resultType = TypeUtil.getType(fieldPath);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery(resultType);
        Root root = cq.from(rootType);

        Path path = root.get(fieldPath);

        cq.select(path);

        return entityManager
                .createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class).fetch(fetchPath, JoinType.LEFT);

        return entityManager
                .createQuery(cq)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    List<User> listEntities() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listEntitiesWithFetchPath(String fetchPath) {
        JoinType joinType = JoinType.LEFT;

        if (fetchPath.contains("<>")) {
            joinType = JoinType.INNER;
            fetchPath = fetchPath.replaceAll("<>", "");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class).fetch(fetchPath, joinType);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithFetchPath(String fetchPath) {
        JoinType joinType = JoinType.LEFT;

        if (fetchPath.contains("<>")) {
            joinType = JoinType.INNER;
            fetchPath = fetchPath.replaceAll("<>", "");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.distinct(true).from(User.class).fetch(fetchPath, joinType);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

}
