package io.hypercriteria.expressionPredicate;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.Predicates.greaterThan;
import io.sample.model.Payment;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
public class FullPredicateInsideWhereTest extends BaseExpressionPredicateTest {

    @Override
    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(greaterThan(fieldPath, value))
                .getResultList((Class<T>) value.getClass());
    }

}
