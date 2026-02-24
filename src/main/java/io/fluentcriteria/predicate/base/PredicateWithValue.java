package io.fluentcriteria.predicate.base;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class PredicateWithValue<T> extends PredicateWithExpression {

    protected ExpressionValue<T> expressionValue;

    public PredicateWithValue(String fieldPath, T value) {
        super(fieldPath);
        this.expressionValue = ExpressionValue.fromValue(value);
    }

    public PredicateWithValue(BaseExpression expression, T value) {
        super(expression);
        this.expressionValue = ExpressionValue.fromValue(value);
    }

    public PredicateWithValue(String fieldPath, BaseExpression rightExpression) {
        super(fieldPath);
        this.expressionValue = ExpressionValue.fromExpression(rightExpression);
    }

    public PredicateWithValue(BaseExpression leftExpression, BaseExpression rightExpression) {
        super(leftExpression);
        this.expressionValue = ExpressionValue.fromExpression(rightExpression);
    }
}
