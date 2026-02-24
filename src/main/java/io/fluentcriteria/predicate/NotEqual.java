/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression; 
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class NotEqual extends PredicateWithValue {
 
    public NotEqual(BaseExpression expression, Object value) {
        super(expression, value);
    } 

    public NotEqual(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().notEqual(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().notEqual(getExpression(ctx), expressionValue.toValue());
    }
}
