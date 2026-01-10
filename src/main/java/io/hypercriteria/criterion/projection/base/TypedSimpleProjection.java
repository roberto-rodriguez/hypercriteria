/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class TypedSimpleProjection<T> extends SimpleProjection {

    protected Class<T> returnType = null;
    protected Optional<SimpleProjection> nestedProjection = Optional.empty();

    public TypedSimpleProjection(String propertyName, Class returnType) {
        super(propertyName);
        this.returnType = returnType;
    }

    public TypedSimpleProjection(SimpleProjection nestedProjection) {
        this.nestedProjection = Optional.of(nestedProjection);
        this.returnType = nestedProjection.getReturnType().get();
    }

    @Override
    public Optional<Class> getReturnType() {
        return Optional.of(returnType);
    }

    @Override
    public void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        query.select(toSelection(builder, query, joinMap));
    }

    @Override
    public Expression toExpression(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        if (nestedProjection.isPresent()) {
            return nestedProjection.get().toExpression(builder, query, joinMap);
        }

        Expression expression = joinMap.get(joinName);

        if (!propertyName.isEmpty()) {
            expression = ((From) expression).get(propertyName);
        }

        return build(builder, expression).as(returnType);
    }

    @Override
    public Selection toSelection(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        return toExpression(builder, query, joinMap)
                .alias(alias);
    }

    public abstract Expression<T> build(CriteriaBuilder builder, Expression expression);

}
