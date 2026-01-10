/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.criterion.projection.base.TypedSimpleProjection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class CountDistinct extends TypedSimpleProjection {

    public CountDistinct() {
        super("", Long.class);//Will produce root
    }

    public CountDistinct(String fieldPath) {
        super(fieldPath, Long.class);
    }

    @Override
    public Expression<Long> build(CriteriaBuilder builder, Expression expression) {
        return builder.countDistinct(expression);
    }
}
