package io.hypercriteria.select.abs;

import io.hypercriteria.util.NumericType;
import io.hypercriteria.util.PathUtil;
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

        Class<?> attributeType = PathUtil.getAttributeType(entityManager, Payment.class, fieldPath);

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

        Class<?> attributeType = PathUtil.getAttributeType(entityManager, User.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression absExpr = cb.abs(payment.get(property));

        cq.select(absExpr);

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    Object absSumByProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = PathUtil.getAttributeType(entityManager, Payment.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<Payment> root = cq.from(Payment.class);

        Expression sumExpr = cb.sum(root.get(fieldPath));
        Expression absExpr = cb.abs(sumExpr);

        cq.select(absExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    Object sumAbsByProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = PathUtil.getAttributeType(entityManager, Payment.class, fieldPath);

        NumericType numericType = NumericType.from(attributeType);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<Payment> root = cq.from(Payment.class);

        Expression absExpr = cb.abs(root.get(fieldPath));
        Expression<?> sumExpr;

        switch (numericType) {
            case BYTE, SHORT, INTEGER, LONG ->
                sumExpr = cb.sumAsLong(absExpr);
            case FLOAT, DOUBLE ->
                sumExpr = cb.sumAsDouble(absExpr);
            case BIG_INTEGER, BIG_DECIMAL ->
                sumExpr = cb.sum((Expression) absExpr);
            default ->
                throw new IllegalStateException("Unexpected type: " + numericType);
        }

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }
}
