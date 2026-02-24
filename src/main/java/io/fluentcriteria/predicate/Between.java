/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.ExpressionValueComparable;
import io.fluentcriteria.predicate.base.PredicateWithExpression;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public class Between<T extends Comparable<T>> extends PredicateWithExpression {

    protected ExpressionValueComparable<T> expressionValueStart;
    protected ExpressionValueComparable<T> expressionValueEnd;

    public Between(BaseExpression expression, T start, T end) {
        super(expression);
        this.expressionValueStart = ExpressionValueComparable.fromValue(start);
        this.expressionValueEnd = ExpressionValueComparable.fromValue(end);
    }

    public Between(BaseExpression expression, BaseExpression expressionStart, BaseExpression expressionEnd) {
        super(expression);
        this.expressionValueStart = ExpressionValueComparable.fromExpression(expressionStart);
        this.expressionValueEnd = ExpressionValueComparable.fromExpression(expressionEnd);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        Expression leftExpression = getExpression(ctx);

        if (expressionValueStart.isExpression()) {
            Expression start = expressionValueStart.toExpression(ctx);
            Expression end = expressionValueEnd.toExpression(ctx);
            return ctx.getCriteriaBuilder().between(leftExpression, start, end);
        }

        T valueStart = expressionValueStart.toValue();
        T valueEnd = expressionValueEnd.toValue();
        return ctx.getCriteriaBuilder().between(leftExpression, valueStart, valueEnd);
    }

}
