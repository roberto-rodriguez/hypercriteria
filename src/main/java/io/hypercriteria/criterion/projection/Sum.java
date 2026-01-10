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
 * @param <T>
 */
public class Sum<T> extends TypedSimpleProjection<T> {

    public Sum(String propertyName, Class<T> returnType) { 
        super(propertyName, returnType);
    }

    @Override
    public Expression<T> build(CriteriaBuilder builder, Expression expression) {
        return builder.sum(expression);
    }
}
