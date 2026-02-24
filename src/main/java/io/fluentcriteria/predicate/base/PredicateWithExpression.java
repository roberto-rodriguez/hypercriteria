package io.fluentcriteria.predicate.base;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.Field;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public abstract class PredicateWithExpression extends BasePredicate {

    protected BaseExpression expression;

    public PredicateWithExpression(String fieldPath) {
        this.expression = new Field(fieldPath);
    }

    public PredicateWithExpression(BaseExpression expression) {
        this.expression = expression;
    }

    protected Expression getExpression(QueryContext ctx) {
        return expression.toExpression(ctx);
    }

}
