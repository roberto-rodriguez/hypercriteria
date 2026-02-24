package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class Ge extends PredicateWithValue<Number> {

    public Ge(BaseExpression expression, Number value) {
        super(expression, value);
    }

    public Ge(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().ge(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().ge(getExpression(ctx), expressionValue.toValue());
    }
}
