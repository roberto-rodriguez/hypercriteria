/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.util.AliasJoinType;
import io.hypercriteria.util.ProjectionBuilder;
import io.hypercriteria.util.TypeUtil;
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
    protected String joinName = "";
    protected String propertyName;
    protected String alias;

    protected Class<?> returnType;

    // a map that will be populated with intermediate path-to-alias mapping
    private final LinkedHashMap<String, AliasJoinType> fieldsMappingToAliasJoinTypeMap = new LinkedHashMap<>();

    public SimpleProjection() {
    }

    public SimpleProjection(String fieldPath) {
        this.fieldPath = fieldPath;

        if (fieldPath.contains(".") || fieldPath.contains(">") || fieldPath.contains("<>")) {
            // joinName will be the last two paths. 
            // Example for a.b.c.d 
            // joinName = c.d
            // fieldPath = d
            // fieldsMappingToAliasJoinTypeMap = {a:a, a.b:b, b.c:c, c.d:d}
            String[] lastTwoPaths = ProjectionBuilder.extractAliasAndJoinType(fieldPath, fieldsMappingToAliasJoinTypeMap).split("\\.");
            this.joinName = lastTwoPaths[0];
            this.propertyName = lastTwoPaths[1];
        } else {
            this.propertyName = fieldPath;
        }
    }

    //To be called directly by Criteria (if it is not included within a ProjectionsList)
    @Override
    public abstract void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    //To be called from ProjectionList 
    public abstract Selection toSelection(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    //Used in Group By
    public abstract Expression toExpression(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    public SimpleProjection as(String alias) {
        setAlias(alias);
        return this;
    }

    public String getAlias() {
        return alias == null ? propertyName : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public Class<?> inferReturnType(EntityManager em, Class<?> rootEntityClass) {
        this.returnType = TypeUtil.inferAttributeType(em, rootEntityClass, fieldPath);
        return this.returnType;
    }

    @Override
    public Optional<Class> getReturnType() {
        return Optional.ofNullable(returnType);
    }

}
