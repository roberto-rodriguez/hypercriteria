package io.fluentcriteria.criterion.predicate.base;

import io.fluentcriteria.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class ComparablePredicate<T extends Comparable<T>> extends ExpressionPredicate {

    protected T value;
    protected BaseExpression expressionValue;

    public ComparablePredicate(String fieldPath, T value) {
        super(fieldPath);
        this.value = value;
    }

    public ComparablePredicate(BaseExpression expression, T value) {
        super(expression);
        this.value = value;
    }

    public ComparablePredicate(String fieldPath, BaseExpression expressionValue) {
        super(fieldPath);
        this.expressionValue = expressionValue;
    }

    public ComparablePredicate(BaseExpression expression, BaseExpression expressionValue) {
        super(expression);
        this.expressionValue = expressionValue;
    }
}
