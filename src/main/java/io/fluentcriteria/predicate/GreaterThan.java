/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.ComparablePredicate;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public class GreaterThan<T extends Comparable<T>> extends ComparablePredicate<T> {

    public GreaterThan(String fieldPath, T value) {
        super(fieldPath, value);
    }

    public GreaterThan(BaseExpression expression, T value) {
        super(expression, value);
    }

    public GreaterThan(String fieldPath, BaseExpression expressionValue) {
        super(fieldPath, expressionValue);
    }

    public GreaterThan(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            return ctx.getCriteriaBuilder().greaterThan(getExpression(ctx), expressionValue.toExpression(ctx));
        }
        return ctx.getCriteriaBuilder().greaterThan(getExpression(ctx), expressionValue.toValue());
    }
}
