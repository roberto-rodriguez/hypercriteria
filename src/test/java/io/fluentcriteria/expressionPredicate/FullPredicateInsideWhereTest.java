package io.fluentcriteria.expressionPredicate;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.Predicates.greaterThan;
import io.sample.model.Payment;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
public class FullPredicateInsideWhereTest extends BaseExpressionPredicateTest {

    @Override
    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(greaterThan(fieldPath, value))
                .getResultList((Class<T>) value.getClass());
    }

}
