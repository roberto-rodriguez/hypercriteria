package io.fluentcriteria.predicate.base;

import io.fluentcriteria.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class BiPredicate extends PredicateWithExpression {

    protected ExpressionValue expressionValue;

    public BiPredicate(String fieldPath, Object value) {
        super(fieldPath);
        this.expressionValue = ExpressionValue.fromValue(value);
    }

    public BiPredicate(BaseExpression expression, Object value) {
        super(expression);
        this.expressionValue = ExpressionValue.fromValue(value);
    }

    public BiPredicate(String fieldPath, BaseExpression expression) {
        super(fieldPath);
        this.expressionValue = ExpressionValue.fromExpression(expression);
    }

    public BiPredicate(BaseExpression expression, BaseExpression expressionValue) {
        super(expression);
        this.expressionValue = ExpressionValue.fromExpression(expression);
    }
}
