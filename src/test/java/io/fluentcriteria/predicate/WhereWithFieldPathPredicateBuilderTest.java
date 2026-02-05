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
public class WhereWithFieldPathPredicateBuilderTest extends BaseExpressionPredicateTest {

    @Override
    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(fieldPath).greaterThan(value)
                .getResultList((Class<T>) value.getClass());
    }

    @Override
    List greaterThanAttributeExpression(String fieldPath, String attributeName) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(fieldPath).greaterThan(field(attributeName))
                .getResultList();
    }

    @Override
    //Keep this methoid here to comply with API, but not really testing: where( field) 
    <T extends Comparable<T>> List<T> absGreaterThanAttributeExpression(String fieldPath, String valueAttributePath) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .where(abs(fieldPath)).greaterThan(field(valueAttributePath))
                .getResultList();
    }

}
