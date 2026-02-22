package io.fluentcriteria.predicate.base;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;
import lombok.Getter;

/**
 *
 * @author rrodriguez
 *
 * Wrapper for either an Expression or a Value
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

    public static ExpressionValue fromValue(Object value) {
        return new ExpressionValue(null, value);
    }

    public boolean isExpression() {
        return expression != null;
    }

    public Expression toExpression(QueryContext ctx) {
        return expression.toExpression(ctx);
    }

    public Object toValue() {
        return value;
    }

}
