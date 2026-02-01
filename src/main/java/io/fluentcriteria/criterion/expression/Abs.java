/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.expression;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class Abs extends BaseExpression {

    public Abs(String fieldPath) {
        super(fieldPath);
    }

    public Abs(BaseExpression nestedExpression) {
        super(nestedExpression);
    }

    @Override
    public Expression build(QueryContext ctx, Expression expression) {
        return ctx.getCriteriaBuilder().abs(expression);
    }
}
