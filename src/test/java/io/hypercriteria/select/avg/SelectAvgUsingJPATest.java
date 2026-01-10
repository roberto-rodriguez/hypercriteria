package io.hypercriteria.select.avg;

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
class SelectAvgUsingJPATest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String propertyName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);

        Root<Payment> root = cq.from(Payment.class);

        Expression<Double> avgExpr = cb.avg(root.get(propertyName));

        cq.select(avgExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String propertyName) {
        String[] parts = propertyName.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression<Double> maxExpr = cb.avg(payment.get(property));

        cq.select(maxExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
