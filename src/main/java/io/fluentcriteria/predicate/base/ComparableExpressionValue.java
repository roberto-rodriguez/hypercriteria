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
 * @param <C>
 */
public class ComparableExpressionValue<C extends Comparable<C>> {

    protected C value;
    protected BaseExpression expression;

    private ComparableExpressionValue(BaseExpression expression, C value) {
        this.value = value;
        this.expression = expression;
    }

    public static ComparableExpressionValue fromExpression(BaseExpression exp) {
        return new ComparableExpressionValue(exp, null);
    }

    public static <C extends Comparable<C>> ComparableExpressionValue fromValue(C value) {
        return new ComparableExpressionValue(null, value);
    }

    public boolean isExpression() {
        return expression != null;
    }

    public Expression toExpression(QueryContext ctx) {
        return expression.toExpression(ctx);
    }

    public C toValue() {
        return value;
    }
}
