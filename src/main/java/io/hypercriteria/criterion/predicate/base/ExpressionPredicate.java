package io.hypercriteria.criterion.predicate.base;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.expression.Property;
import io.hypercriteria.criterion.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public abstract class ExpressionPredicate extends BasePredicate {

    protected BaseExpression expression;

    public ExpressionPredicate(String fieldPath) {
        this.expression
                = new Property(fieldPath);
    }

    public ExpressionPredicate(BaseExpression expression) {
        this.expression = expression;
    }

    protected Expression getExpression(QueryContext ctx) {
        return expression.toExpression(ctx);
    }

}
