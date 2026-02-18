package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class IsTrue extends PredicateWithExpression {

    public IsTrue(String fieldPath) {
        super(fieldPath);
    }

    public IsTrue(BaseExpression expression) {
        super(expression);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        return ctx.getCriteriaBuilder().isTrue(getExpression(ctx));
    }

}
