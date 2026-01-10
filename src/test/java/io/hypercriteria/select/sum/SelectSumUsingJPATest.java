package io.hypercriteria.select.sum;
 
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
class SelectSumUsingJPATest extends BaseSelectSumTest {

    @Override
    <T extends Number> T sumByProperty(String propertyName, Class<T> resultType) { 
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<Payment> root = cq.from(Payment.class);

        Expression<T> sumExpr = cb.sum(root.get(propertyName));

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

     @Override
    <T extends Number> T sumByNestedProperty(String propertyName, Class<T> resultType) {
        String[] parts = propertyName.split("\\.");
        String entity = parts[0];
        String property = parts[1];
 
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression<T> sumExpr = cb.sum(payment.get(property));

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
