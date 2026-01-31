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
public class CountDistinct extends BaseExpression {

    public CountDistinct() {
        this("*");//Will produce root
    }

    public CountDistinct(String fieldPath) {
        super(fieldPath, t -> Long.class);
    }

    @Override
    public Expression<Long> build(QueryContext ctx, Expression expression) {
        return ctx.getCriteriaBuilder().countDistinct(expression);
    }
}
