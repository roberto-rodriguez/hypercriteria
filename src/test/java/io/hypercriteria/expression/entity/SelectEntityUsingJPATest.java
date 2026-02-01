package io.hypercriteria.expression.entity;

import io.sample.model.Address;
import io.sample.model.User;
import io.utility.TypeUtil;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
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
        Root<User> root = cq.distinct(true).from(User.class);
        Join<User, Address> addressJoin = root.join(fetchPath, joinType);

        cb.equal(addressJoin.get("zipcode"), 123456);
//        join.fe
//        Fetch<User, Address> fetch = root.fetch(fetchPath, joinType);

//        fetch.
        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> sample() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);

        Fetch<User, Address> addressFetch = root.fetch("address", JoinType.LEFT);

        Join<User, Address> addressJoin
                = (Join<User, Address>) addressFetch;

        cq.where(
                cb.like(addressJoin.get("zipcode"), "123456")
        );

        return entityManager.createQuery(cq).getResultList();
    }

}
