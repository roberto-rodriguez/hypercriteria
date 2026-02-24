package io.fluentcriteria.predicate.base;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 *
 * Wrapper for either an Expression or a Value where the Value has to be
 * COmparable
 * @param <T>
 */
public class ExpressionValue<T> {

    protected T value;
    protected BaseExpression expression;

    private ExpressionValue(BaseExpression expression, T value) {
        this.value = value;
        this.expression = expression;
    }

    public static ExpressionValue fromExpression(BaseExpression exp) {
        return new ExpressionValue(exp, null);
    }

    public static <T> ExpressionValue fromValue(T value) {
        return new ExpressionValue(null, value);
    }

    public boolean isExpression() {
        return expression != null;
    }

    public Expression toExpression(QueryContext ctx) {
        return expression.toExpression(ctx);
    }

    public T toValue() {
        return value;
    }
}
