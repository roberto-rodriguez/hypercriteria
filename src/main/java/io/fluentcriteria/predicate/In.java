/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.PredicateWithExpression;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
public class In extends PredicateWithExpression {

    private Collection values;
    private List<BaseExpression> expressions;

    public In(BaseExpression expression, Collection values) {
        super(expression);
        this.values = values;
    }

    public In(BaseExpression expression, List<BaseExpression> expressions) {
        super(expression);
        this.expressions = expressions;
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        Expression leftExpression = getExpression(ctx);
        if (expressions == null) {
            return leftExpression.in(values);
        } else {
            if (expressions.size() == 1) {
                return leftExpression.in(expressions.get(0));
            } else {
                return leftExpression.in(expressions.toArray());
            }
        }
    }
}
