/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.criterion;

import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author rrodriguez
 */
public class ProjectionList //implements Projection 
{

    private final List<BaseExpression> projections = new ArrayList<>();
    private final Optional<Class> returnType;

    public ProjectionList() {
        this.returnType = Optional.empty();
    }

    public ProjectionList(Class dtoType) {
        this.returnType = Optional.of(dtoType);
    }

    public ProjectionList add(BaseExpression projection) {
        projections.add(projection);
        return this;
    }

    public ProjectionList add(BaseExpression projection, String alias) {
        projection.as(alias);
        projections.add(projection);
        return this;
    }

//    @Override
    public void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
//        query.multiselect(projections
//                .stream()
//                .map(p -> p.toSelection(builder, query, joinMap))
//                .collect(Collectors.toList()))
//                .distinct(criteria.isDistinct());
    }

//    @Override
    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap) {
//        Expression[] expressionsWithGroupBy
//                = projections.stream()
//                        .filter(SimpleProjection::isGroupBy)
//                        .map(p -> p.toExpression(builder, query, joinMap))
//                        .toArray(Expression[]::new);
//
//        if (expressionsWithGroupBy.length > 0) {
//            query.groupBy(expressionsWithGroupBy);
//        }
    }

//    @Override
    public Optional<Class> getReturnType() {
        return returnType;
    }

    public List<BaseExpression> getProjections() {
        return projections;
    }
}
