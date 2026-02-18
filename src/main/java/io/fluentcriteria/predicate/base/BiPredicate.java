package io.fluentcriteria.predicate.base;

import io.fluentcriteria.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class BiPredicate extends PredicateWithExpression {

    protected Object value;
    protected BaseExpression expressionValue;

    public BiPredicate(String fieldPath, Object value) {
        super(fieldPath);
        this.value = value;
    }

    public BiPredicate(BaseExpression expression, Object value) {
        super(expression);
        this.value = value;
    }

    public BiPredicate(String fieldPath, BaseExpression expressionValue) {
        super(fieldPath);
        this.expressionValue = expressionValue;
    }

    public BiPredicate(BaseExpression expression, BaseExpression expressionValue) {
        super(expression);
        this.expressionValue = expressionValue;
    }
}
