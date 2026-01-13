package io.hypercriteria.select.sum;

import io.hypercriteria.util.NumericType;
import static io.hypercriteria.util.NumericType.BIG_DECIMAL;
import static io.hypercriteria.util.NumericType.BIG_INTEGER;
import static io.hypercriteria.util.NumericType.BYTE;
import static io.hypercriteria.util.NumericType.DOUBLE;
import static io.hypercriteria.util.NumericType.FLOAT;
import static io.hypercriteria.util.NumericType.INTEGER;
import static io.hypercriteria.util.NumericType.LONG;
import static io.hypercriteria.util.NumericType.SHORT;
import io.hypercriteria.util.TypeUtil;
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
    Object sumByProperty(String fieldPath) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, Payment.class, fieldPath);

        NumericType numericType = NumericType.from(attributeType);

        CriteriaQuery cq = cb.createQuery(numericType.getPromotionTypeWhenSuming());

        Root<Payment> root = cq.from(Payment.class);

        Expression<?> sumExpr;

        switch (numericType) {
            case BYTE, SHORT, INTEGER, LONG ->
                sumExpr = cb.sumAsLong(root.get(fieldPath));
            case FLOAT, DOUBLE ->
                sumExpr = cb.sumAsDouble(root.get(fieldPath));
            case BIG_INTEGER, BIG_DECIMAL ->
                sumExpr = cb.sum(root.get(fieldPath));
            default ->
                throw new IllegalStateException("Unexpected type: " + numericType);
        }

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    Object sumByNestedProperty(String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, User.class, fieldPath);

        NumericType numericType = NumericType.from(attributeType);

        CriteriaQuery cq = cb.createQuery(numericType.getPromotionTypeWhenSuming());

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression<?> sumExpr;

        switch (numericType) {
            case BYTE, SHORT, INTEGER, LONG ->
                sumExpr = cb.sumAsLong(payment.get(property));
            case FLOAT, DOUBLE ->
                sumExpr = cb.sumAsDouble(payment.get(property));
            case BIG_INTEGER, BIG_DECIMAL ->
                sumExpr = cb.sum((Expression) payment.get(property));
            default ->
                throw new IllegalStateException("Unexpected type: " + numericType);
        }

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
