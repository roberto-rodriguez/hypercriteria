package io.hypercriteria.select.abs;

import io.hypercriteria.util.TypeUtil;
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
    List absByProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, Payment.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<Payment> root = cq.from(Payment.class);

        Expression absExpr = cb.abs(root.get(fieldPath));

        cq.select(absExpr);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    List absByNestedProperty(String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, User.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression absExpr = cb.abs(payment.get(property));

        cq.select(absExpr);

        return entityManager.createQuery(cq).getResultList();
    }

//    @Override
//    <T extends Number, R extends Number> R absSumByProperty(String fieldPath, Class<T> resultType) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(resultType);
//
//        Root<Payment> root = cq.from(Payment.class);
//
//        Expression<T> sumExpr = cb.sum(root.get(fieldPath));
//        Expression<T> absExpr = cb.abs(sumExpr);
//
//        cq.select(absExpr);
//
//        return entityManager.createQuery(cq).getSingleResult();
//    }
}
