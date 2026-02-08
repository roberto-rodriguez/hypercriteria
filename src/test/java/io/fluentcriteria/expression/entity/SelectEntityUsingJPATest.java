package io.fluentcriteria.expression.entity;

import io.sample.model.Address;
import io.sample.model.Payment;
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
    List<User> listDistinctEntities() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Join<User, Payment> joinPayment = root.join("payments", JoinType.LEFT);

//        cq.where(cb.isNotNull(joinPayment));
        cq.distinct(true);
        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftJoinPath(String joinPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class).join(joinPath, JoinType.LEFT);
        cq.distinct(true);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerJoinPath(String joinPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);
        root.join(joinPath, JoinType.INNER);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftFetchPath(String fetchPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        cq.from(User.class).fetch(fetchPath, JoinType.LEFT);
        cq.distinct(true);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerFetchPath(String fetchPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);
        root.fetch(fetchPath, JoinType.INNER);

        return entityManager
                .createQuery(cq)
                .getResultList();
    }

    @Override
    User selectUserWithMultipleFetches() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);

        root.fetch("payments", JoinType.LEFT);
        root.fetch("address", JoinType.LEFT);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    User duplicatedAlias_throwsException() {
        throw new IllegalArgumentException();
    }

    @Override
    User noFromClause_throwsException() {
        throw new IllegalArgumentException();
    }

    @Override
    Long fetchWithProjection_throwsException() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<User> root = cq.distinct(true).from(User.class);

        Path<Long> path = root.get("id");

        cq.select(path);

        root.fetch("address", JoinType.LEFT);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    User fetchOneToOneAddress() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        root.fetch("address", JoinType.LEFT);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    User fetchNestedManyToOne() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);

        Fetch<User, Address> addressFetch = root.fetch("address", JoinType.LEFT);
        addressFetch.fetch("state", JoinType.LEFT);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    Payment fetchCollectionThenToOne() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
        Root<Payment> root = cq.distinct(true).from(Payment.class);

        Fetch<Payment, User> userFetch = root.fetch("user", JoinType.LEFT);
        Fetch<User, Address> addressFetch = userFetch.fetch("address", JoinType.LEFT);

        return entityManager.createQuery(cq).setMaxResults(1).getSingleResult();
    }

    @Override
    User fetchMultipleRelationsMixed() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.distinct(true).from(User.class);

        root.fetch("payments", JoinType.LEFT);
        Fetch<User, Address> addressFetch = root.fetch("address", JoinType.LEFT);
        addressFetch.fetch("state", JoinType.LEFT);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    User duplicateFetchPathIsIgnored() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        root.fetch("payments", JoinType.LEFT);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    User joinAndFetchSamePath() {
        return duplicateFetchPathIsIgnored();
    }

    @Override
    User innerFetchToOne() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        root.fetch("address", JoinType.LEFT);
        return entityManager.createQuery(cq).getSingleResult();
    }
}
