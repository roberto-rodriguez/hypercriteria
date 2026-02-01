/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.expression;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class Count extends BaseExpression {

    public Count() {
        this("*");//Placeholder to indicate Path expression to return root.
    }

    public Count(String fieldPath) {
        super(fieldPath, t -> Long.class);
    }

    @Override
    public Expression<Long> build(QueryContext ctx, Expression expression) {
        return ctx.getCriteriaBuilder().count(expression);
    }
}
