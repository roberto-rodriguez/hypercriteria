package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class Lt extends PredicateWithValue<Number> {

    public Lt(BaseExpression expression, Number value) {
        super(expression, value);
    }

    public Lt(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().lt(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().lt(getExpression(ctx), expressionValue.toValue());
    }
}
