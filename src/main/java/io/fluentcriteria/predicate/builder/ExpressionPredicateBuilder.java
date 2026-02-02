package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.GreaterThan;
import io.fluentcriteria.predicate.base.BasePredicate;

/**
 *
 * @author rrodriguez
 */
public abstract class ExpressionPredicateBuilder {

    protected Criteria criteria;
    protected BaseExpression leftExpression;

    public ExpressionPredicateBuilder(Criteria criteria, BaseExpression leftExpression) {
        this.criteria = criteria;
        this.leftExpression = leftExpression;
    }

    protected abstract Criteria setPredicate(BasePredicate predicate);

    public <T extends Comparable<T>> Criteria greaterThan(T value) {
        return setPredicate(new GreaterThan<>(leftExpression, value));
    }

    public <T extends Comparable<T>> Criteria greaterThan(BaseExpression expressionValue) {
        return setPredicate(new GreaterThan<>(leftExpression, expressionValue));
    }
    
    // All other predicate methods here
    
}
