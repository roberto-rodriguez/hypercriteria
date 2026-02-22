package io.fluentcriteria.predicate.base;

import io.fluentcriteria.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class ComparablePredicate<T extends Comparable<T>> extends PredicateWithExpression {

    protected ComparableExpressionValue<T> expressionValue;

    public ComparablePredicate(String fieldPath, T value) {
        super(fieldPath);
        this.expressionValue = ComparableExpressionValue.fromValue(value);
    }

    public ComparablePredicate(BaseExpression expression, T value) {
        super(expression);
        this.expressionValue = ComparableExpressionValue.fromValue(value);
    }

    public ComparablePredicate(String fieldPath, BaseExpression rightExpression) {
        super(fieldPath);
        this.expressionValue = ComparableExpressionValue.fromExpression(rightExpression);
    }

    public ComparablePredicate(BaseExpression leftExpression, BaseExpression rightExpression) {
        super(leftExpression);
        this.expressionValue = ComparableExpressionValue.fromExpression(rightExpression);
    }
}
