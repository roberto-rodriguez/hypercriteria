/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.criterion.projection.base.TypedSimpleProjection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public class Avg extends TypedSimpleProjection {

    public Avg(String fieldPath) {
        super(fieldPath, Double.class);
    }

    @Override
    public Expression<Double> build(CriteriaBuilder builder, Expression expression) {
        return builder.avg(expression);
    }

    @Override
    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
    }
}
