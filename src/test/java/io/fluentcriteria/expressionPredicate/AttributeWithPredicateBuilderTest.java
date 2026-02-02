package io.fluentcriteria.expressionPredicate;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.Predicates.greaterThan;
import static io.fluentcriteria.FluentCriteria.abs;
import static io.fluentcriteria.FluentCriteria.attribute;
import io.sample.model.Payment;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
public class AttributeWithPredicateBuilderTest extends BaseExpressionPredicateTest {

    @Override
    <T extends Comparable<T>> List<T> greaterThanProperty(String fieldPath, T value) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(attribute(fieldPath).greaterThan(value))
                .getResultList((Class<T>) value.getClass());
    }

    @Override
    List greaterThanAttributeExpression(String fieldPath, String attrName) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(Payment.class)
                .where(attribute(fieldPath).greaterThan(attribute(attrName)))
                .getResultList();
    }

    @Override
    <T extends Comparable<T>> List<T> absGreaterThanAttributeExpression(String fieldPath, String attrName) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .where(abs(fieldPath).greaterThan(attribute(attrName)))
                .getResultList();
    }

}
