package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class Le extends PredicateWithValue<Number> {

    public Le(BaseExpression expression, Number value) {
        super(expression, value);
    }

    public Le(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().le(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().le(getExpression(ctx), expressionValue.toValue());
    }
}
