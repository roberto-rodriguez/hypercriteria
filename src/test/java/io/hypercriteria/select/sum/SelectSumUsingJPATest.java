package io.hypercriteria.select.sum;

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

        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, User.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<Payment> root = cq.from(Payment.class);

        Expression sumExpr = cb.sum(root.get(fieldPath));

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

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression sumExpr = cb.sum(payment.get(property));

        cq.select(sumExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
