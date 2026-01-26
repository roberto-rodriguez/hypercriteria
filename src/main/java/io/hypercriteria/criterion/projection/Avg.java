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
public class Avg extends Projection {

    public Avg(String fieldPath) {
        super(fieldPath, t -> Double.class);
    }

    @Override
    public Expression<Double> build(QueryContext ctx, Expression expression) {
        return ctx.getCriteriaBuilder().avg(expression);
    }
}
