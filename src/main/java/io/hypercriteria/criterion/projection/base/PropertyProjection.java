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
        validatePath(); 
        return getJoin(joinMap).get(pathInfo.getAttributeName().get());
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

    @Override
    protected void validatePath() throws IllegalArgumentException {
        if (pathInfo.getLastJoin() == null) {
            //This should never happens, and indicates a Framework (not user) issue.
            throw new IllegalArgumentException(
                    String.format("Invalid field path '%s', unexpected last join as null here.", fieldPath)
            );
        }
        if (pathInfo.getAttributeName().isEmpty()) {
            //This should never happens, and indicated a Framework (not user) issue.
            throw new IllegalArgumentException(
                    String.format("Invalid field path '%s', attribute name should not be empty.", fieldPath)
            );
        }

        if (pathInfo.endsInAssociation()) {
            throw new IllegalArgumentException(
                    String.format("Invalid field path '%s', property projections must refer to attributes, no associations.", fieldPath)
            );
        }
    }
}
