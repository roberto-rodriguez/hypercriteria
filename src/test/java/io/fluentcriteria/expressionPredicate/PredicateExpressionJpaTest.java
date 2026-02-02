package io.fluentcriteria.expressionPredicate;

import io.fluentcriteria.predicate.graterThan.BaseGreaterThanTest;
import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 *
 * @author rrodriguez
 */
public class PredicateExpressionJpaTest 
      //  extends BaseExpressionPredicateTest 
{

//    @Override
//    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery((Class<T>) value.getClass());
//
//        Root<Payment> root = cq.from(Payment.class);
//        Path<T> path = root.get(fieldPath);
//
//        cq.select(path)
//                .where(cb.greaterThan(path, value));
//        return entityManager.createQuery(cq).getResultList();
//    }

}
