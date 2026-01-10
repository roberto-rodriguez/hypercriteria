/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.criterion.projection.base.SimpleProjection;
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
public class Abs<T> extends TypedSimpleProjection<T> {

    public Abs(String propertyName, Class<T> returnType) {
        super(propertyName, returnType);
    }

    public Abs(SimpleProjection nestedProjection) {
        super(nestedProjection);
    }

    @Override
    public Expression<T> build(CriteriaBuilder builder, Expression expression) {
        return builder.abs(expression);
    }

    @Override
    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
    }
}
