package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class IsFalse extends PredicateWithExpression {

    public IsFalse(BaseExpression expression) {
        super(expression);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        return ctx.getCriteriaBuilder().isFalse(getExpression(ctx));
    }

}
