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
import javax.persistence.EntityManager;

/**
 *
 * @author rrodriguez
 */
public abstract class TypedSimpleProjection extends SimpleProjection {

    protected Optional<SimpleProjection> nestedProjection = Optional.empty();

    public TypedSimpleProjection(String propertyName) {
        super(propertyName);
    }

    public TypedSimpleProjection(String propertyName, Class<?> returnType) {
        super(propertyName);
        this.returnType = returnType;
    }

    public TypedSimpleProjection(SimpleProjection nestedProjection) {
        this.nestedProjection = Optional.of(nestedProjection);

    }

    @Override
    public void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        query.select(toSelection(builder, query, joinMap));
    }

    @Override
    public Expression toExpression(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {

        Expression expression;

        if (nestedProjection.isPresent()) {
            expression = nestedProjection.get().toExpression(builder, query, joinMap);
        } else {
            expression = joinMap.get(joinName);

            if (!propertyName.isEmpty()) {
                expression = ((From) expression).get(propertyName);
            }
        }

        return build(builder, expression).as(returnType);
    }

    @Override
    public Selection toSelection(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        return toExpression(builder, query, joinMap)
                .alias(alias);
    }

    public abstract Expression build(CriteriaBuilder builder, Expression expression);

    @Override
    public Class<?> inferReturnType(EntityManager em, Class<?> rootEntityClass) {
        if (this.returnType == null) {//Projectins like Avg and Count have a pre-defined returnType
            if (nestedProjection.isPresent()) {
                this.returnType = nestedProjection.get().inferReturnType(em, rootEntityClass);
            } else {
                super.inferReturnType(em, rootEntityClass);
            }
        }

        return this.returnType;
    }

    @Override
    public Optional<Class> getReturnType() {
        return Optional.ofNullable(returnType);
    }

}
