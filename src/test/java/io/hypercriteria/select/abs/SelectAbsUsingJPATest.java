package io.hypercriteria.select.abs;

import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;
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
class SelectAbsUsingJPATest extends BaseSelectAbsTest {

    @Override
    <T extends Number> List<T> absByProperty(String propertyName, Class<T> resultType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<Payment> root = cq.from(Payment.class);

        Expression<T> absExpr = cb.abs(root.get(propertyName));

        cq.select(absExpr);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    <T extends Number> List<T> absByNestedProperty(String propertyName, Class<T> resultType) {
        String[] parts = propertyName.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(resultType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression<T> absExpr = cb.abs(payment.get(property));

        cq.select(absExpr);

        return entityManager.createQuery(cq).getResultList();
    }

//    @Override
//    <T extends Number, R extends Number> R absSumByProperty(String propertyName, Class<T> resultType) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(resultType);
//
//        Root<Payment> root = cq.from(Payment.class);
//
//        Expression<T> sumExpr = cb.sum(root.get(propertyName));
//        Expression<T> absExpr = cb.abs(sumExpr);
//
//        cq.select(absExpr);
//
//        return entityManager.createQuery(cq).getSingleResult();
//    }

}
