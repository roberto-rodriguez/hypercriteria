/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.projection.base.Projection;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class Count extends Projection {

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
