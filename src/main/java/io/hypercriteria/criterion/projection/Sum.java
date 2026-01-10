/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.criterion.projection.base.TypedSimpleProjection;
import io.hypercriteria.util.NumericType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez 
 */
public class Sum<T> extends TypedSimpleProjection {

    public Sum(String fieldPath) {
        super(fieldPath);
    }

    @Override
    public Expression<T> build(CriteriaBuilder builder, Expression expression) {
        return builder.sum(expression);
    }

    @Override
    public void setReturnType(Class returnType) {
        NumericType numericType = NumericType.from(returnType);
        super.setReturnType(numericType.getPromotionTypeWhenSuming());
    }

}
