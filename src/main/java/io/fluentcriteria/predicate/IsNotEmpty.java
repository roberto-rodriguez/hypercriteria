/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithExpression;

import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class IsNotEmpty extends PredicateWithExpression {
 
    public IsNotEmpty(BaseExpression expression) {
        super(expression);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        return ctx.getCriteriaBuilder().isNotEmpty(getExpression(ctx));
    }

}
