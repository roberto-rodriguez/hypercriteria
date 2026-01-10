/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;
import java.util.Map;
import java.util.Optional;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class PropertyProjection extends SimpleProjection {

    private boolean groupBy;

    public PropertyProjection(String propertyName) {
        super(propertyName);
    }

    @Override
    public PropertyProjection as(String alias) {
        setAlias(alias);
        return this;
    }

    @Override
    public void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        query.select(toSelection(builder, query, joinMap)).distinct(criteria.isDistinct());
    }

    @Override
    public Expression toExpression(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        return joinMap.get(joinName).get(propertyName);
    }

    @Override
    public Selection toSelection(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        try {
            return joinMap.get(joinName).get(propertyName).alias(alias);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Exception applying PropertyProjection:: %s.%s as alias %s", joinName, propertyName, alias));
        }
    }

    @Override
    public Optional<Class> getReturnType() {
        return Optional.of(Object.class);
    }

    public void setGroupBy(boolean groupBy) {
        this.groupBy = groupBy;
    }

    @Override
    public boolean isGroupBy() {
        return groupBy;
    }

    @Override
    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        if (groupBy) {
            query.groupBy(toExpression(builder, query, joinMap));
        }
    }
}
