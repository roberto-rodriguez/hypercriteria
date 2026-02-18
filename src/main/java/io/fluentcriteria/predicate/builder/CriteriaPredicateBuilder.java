package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.predicate.IsTrue;
import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.Field;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.Equal;
import io.fluentcriteria.predicate.GreaterThan;
import io.fluentcriteria.predicate.base.BasePredicate;

/**
 *
 * @author rrodriguez
 */
public abstract class CriteriaPredicateBuilder implements PredicateBuilder<Criteria> {

    protected Criteria criteria;
    protected BaseExpression leftExpression;

    public CriteriaPredicateBuilder(Criteria criteria, BaseExpression leftExpression) {
        this.criteria = criteria;
        this.leftExpression = leftExpression;
    }

    protected abstract Criteria setPredicate(BasePredicate predicate);

    @Override
    public Criteria isTrue(BaseExpression expressionValue) {
        return setPredicate(new IsTrue(leftExpression));
    }

    @Override
    public Criteria isTrueField(String fieldPath) {
        return setPredicate(new IsTrue(new Field(fieldPath)));
    }

    //Greater Than
    @Override
    public <T extends Comparable<T>> Criteria greaterThan(T value) {
        return setPredicate(new GreaterThan<>(leftExpression, value));
    }

    @Override
    public Criteria greaterThan(BaseExpression expressionValue) {
        return setPredicate(new GreaterThan<>(leftExpression, expressionValue));
    }

    @Override
    public Criteria greaterThanField(String fieldPath) {
        return greaterThan(new Field(fieldPath));
    }

    //Equal
    @Override
    public Criteria equal(Object value) {
        return setPredicate(new Equal(leftExpression, value));
    }

    @Override
    public Criteria equal(BaseExpression expressionValue) {
        return setPredicate(new Equal(leftExpression, expressionValue));
    }

    @Override
    public Criteria equalField(String fieldPath) {
        return equal(new Field(fieldPath));
    }
}
