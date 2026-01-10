package io.hypercriteria.select.min;

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
class SelectMinUsingJPATest extends BaseSelectMinTest {

    @Override
   Object minByProperty(String fieldPath ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, Payment.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<Payment> root = cq.from(Payment.class);

        Expression minExpr = cb.min(root.get(fieldPath));

        cq.select(minExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
  Object minByNestedProperty(String fieldPath ) {
        String[] parts = fieldPath.split("\\.");
        String entity = parts[0];
        String property = parts[1];

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
          Class<?> attributeType = TypeUtil.inferAttributeType(entityManager, User.class, fieldPath);

        CriteriaQuery cq = cb.createQuery(attributeType);

        Root<User> root = cq.from(User.class);
        Join<User, Payment> payment = root.join(entity, JoinType.LEFT);

        Expression  minExpr = cb.min(payment.get(property));

        cq.select(minExpr);

        return entityManager.createQuery(cq).getSingleResult();
    }

}
