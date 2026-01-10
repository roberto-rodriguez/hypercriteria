package io.hypercriteria.select.max;

import io.sample.model.Payment;
import io.sample.model.User;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingJPATest extends BaseSelectMaxTest {

    @Override
    <T extends Number> T maxByProperty(String propertyName, Class<T> resultType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<Payment> root = cq.from(Payment.class);

        Expression<T> maxExpr = cb.max(root.get(propertyName));

        cq.select(maxExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    <T extends Number> T maxByNestedProperty(String propertyName, Class<T> resultType) {
        String[] parts = propertyName.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression<T> maxExpr = cb.max(payment.get(property));

        cq.select(maxExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
