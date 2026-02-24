package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class Gt extends PredicateWithValue<Number> {

    public Gt(BaseExpression expression, Number value) {
        super(expression, value);
    }

    public Gt(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().gt(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().gt(getExpression(ctx), expressionValue.toValue());
    }
}
