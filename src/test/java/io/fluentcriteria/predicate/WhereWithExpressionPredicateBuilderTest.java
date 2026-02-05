package io.fluentcriteria.predicate;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.abs;
import io.sample.model.Payment;
import java.util.List;
import static io.fluentcriteria.FluentCriteria.field;

/**
 *
 * @author rrodriguez
 */
public class WhereWithExpressionPredicateBuilderTest extends BaseExpressionPredicateTest {

    @Override
    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(field(fieldPath)).greaterThan(value)
                .getResultList((Class<T>) value.getClass());
    }

    @Override
    List greaterThanAttributeExpression(String fieldPath, String attributeName) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(field(fieldPath)).greaterThan(field(attributeName))
                .getResultList();
    }

    @Override
    <T extends Comparable<T>> List<T> absGreaterThanAttributeExpression(String fieldPath, String rightAttributePath) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .where(abs(fieldPath)).greaterThan(field(rightAttributePath))
                .getResultList();
    }

}
