/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.util.PathInfo;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;

/**
 *
 * @author rrodriguez
 */
public abstract class TypedSimpleProjection extends SimpleProjection {

    protected Optional<SimpleProjection> nestedProjection = Optional.empty();

    public TypedSimpleProjection(String fieldPath) {
        super(fieldPath);
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
        validatePath();

        Expression expression;

        if (nestedProjection.isPresent()) {
            expression = nestedProjection.get().toExpression(builder, query, joinMap);
        } else {
            expression = getJoin(joinMap);

            if (pathInfo.getAttributeName().isPresent()) {
                expression = ((From) expression).get(pathInfo.getAttributeName().get());
            }
        }

        return build(builder, expression).as(getReturnType().get());
    }

    public abstract Expression build(CriteriaBuilder builder, Expression expression);

    @Override
    public PathInfo getPathInfo(EntityManager em, Class<?> rootEntityClass) {
        if (nestedProjection.isPresent()) {
            this.pathInfo = nestedProjection.get().getPathInfo(em, rootEntityClass);

            //To be overriden by:
            // Sum: returns promotionReturnType
            // Avg: returns Double
            // Count: returns Long
            updateReturnType();
        } else {
            super.getPathInfo(em, rootEntityClass);
        }

        return this.pathInfo;
    }

    @Override
    protected void validatePath() throws IllegalArgumentException {
        //By default the path for typed expressions should not refer to an asociation,
        // except specific cases (like count("payments"))
        //Projections supporting path to asociations will override this.
        if (pathInfo.endsInAssociation()) {
            throw new IllegalArgumentException(
                    String.format("Invalid field path '%s', property projections must refer to attributes, no associations.", fieldPath)
            );
        }
    }

}
