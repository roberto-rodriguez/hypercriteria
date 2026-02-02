/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.expression;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.GreaterThan;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class Attribute extends BaseExpression {

    public Attribute(String propertyName) {
        super(propertyName);
    }

    @Override
    public Expression build(QueryContext ctx, Expression expression) {
        return expression;
    }

    
}
