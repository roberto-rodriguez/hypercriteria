package io.fluentcriteria.predicate;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.Predicates.greaterThan;
import static io.fluentcriteria.FluentCriteria.abs;
import io.sample.model.Payment;
import java.util.List;
import static io.fluentcriteria.FluentCriteria.field;

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

    @Override
    List greaterThanAttributeExpression(String leftAttributePath, String rightAttributePath) {
        return FluentCriteria.using(entityManager)
                .select(leftAttributePath)
                .from(Payment.class)
                .where(greaterThan(leftAttributePath, field(rightAttributePath)))
                .getResultList();
    }

    @Override
    <T extends Comparable<T>> List<T> absGreaterThanAttributeExpression(String fieldPath, String rightAttributePath) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .where(greaterThan(abs(fieldPath), field(rightAttributePath)))
                .getResultList();
    }
}
