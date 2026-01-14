/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.util.AliasJoinType;
import io.hypercriteria.util.PathInfo;
import io.hypercriteria.util.ProjectionBuilder;
import io.hypercriteria.util.PathUtil;
import java.util.LinkedHashMap;
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
public abstract class SimpleProjection implements Projection {

    protected String fieldPath;
    protected PathInfo pathInfo;

    protected Optional<String> alias = Optional.empty();
 
    // a map that will be populated with intermediate path-to-alias mapping
    private final LinkedHashMap<String, AliasJoinType> fieldsMappingToAliasJoinTypeMap = new LinkedHashMap<>();

    public SimpleProjection() {
    }

    public SimpleProjection(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    //To be called from toExpression
    protected abstract void validatePath() throws IllegalArgumentException;

    //To be called directly by Criteria (if it is not included within a ProjectionsList)
    @Override
    public abstract void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    //To be called from ProjectionList  
    public Selection toSelection(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
        Expression expression = toExpression(builder, query, joinMap);

        if (alias.isPresent()) {
            expression.alias(alias.get());
        }
        return expression;
    }

    //Used in Group By
    public abstract Expression toExpression(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    public SimpleProjection as(String alias) {
        setAlias(alias);
        return this;
    }

    public String getAlias() {
        return alias.orElse(pathInfo.getAlias());
    }

    public void setAlias(String alias) {
        this.alias = Optional.ofNullable(alias);
    }

    public boolean isGroupBy() {
        return false;
    }

    @Override
    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
    }

    public void createJoinsFromNestedPath(Criteria criteria) {

    }

    public LinkedHashMap<String, AliasJoinType> getFieldsMappingToAliasJoinTypeMap() {
        return fieldsMappingToAliasJoinTypeMap;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public PathInfo getPathInfo(EntityManager em, Class<?> rootEntityClass) {
        this.pathInfo = PathUtil.getPathInfo(em, rootEntityClass, fieldPath, fieldsMappingToAliasJoinTypeMap);

        //To be overriden by:
        // Sum: returns promotionReturnType
        // Avg: returns Double
        // Count: returns Long
        updateReturnType();

        return this.pathInfo;
    }
 
    protected void updateReturnType() {
    }

    @Override
    public Optional<Class> getReturnType() {
        return Optional.ofNullable(pathInfo.getJavaType());
    }

    protected From getJoin(Map<String, From> joinMap) {
        From join = joinMap.get(pathInfo.getLastJoin());

        if (join == null) {
            //This should never happens, and indicates a Framework (not user) issue.
            throw new IllegalArgumentException(
                    String.format("Invalid field path '%s', not join '%s' was found.", fieldPath, pathInfo.getLastJoin())
            );
        }
        return join;
    }

}
