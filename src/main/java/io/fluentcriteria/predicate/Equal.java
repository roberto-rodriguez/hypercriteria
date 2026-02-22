/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.BiPredicate;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public class Equal extends BiPredicate {

    public Equal(String fieldPath, Object value) {
        super(fieldPath, value);
    }

    public Equal(BaseExpression expression, Object value) {
        super(expression, value);
    }

    public Equal(String fieldPath, BaseExpression expressionValue) {
        super(fieldPath, expressionValue);
    }

    public Equal(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().equal(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().equal(getExpression(ctx), expressionValue.toValue());
    }
}
