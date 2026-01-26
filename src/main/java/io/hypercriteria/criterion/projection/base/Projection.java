/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.base.BaseExpression;
import io.hypercriteria.context.PathExpression;
import io.hypercriteria.context.QueryContext;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Selection;

/**
 *
 * @author rrodriguez
 */
public abstract class Projection extends BaseExpression {

    protected String alias;

    //To be called for Expressions with nested expressions
    public Projection() {
    }

    public Projection(String fieldPath) {
        this.pathExpression = new PathExpression(fieldPath, Function.identity());
    }

    public Projection(String fieldPath, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    public Projection(Projection nestedProjection) {
        this(nestedProjection, Function.identity());
    }

    public Projection(Projection nestedProjection, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.nestedProjection = Optional.of(nestedProjection);

        String fieldPath = this.nestedProjection.get().getPathExpression().getRawPath();

        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    //To be called directly by Criteria (if it is not included within a ProjectionsList) 
    public void apply(QueryContext ctx, CriteriaQuery criteriaQuery) {
        criteriaQuery.select(
                toExpression(ctx).as(getReturnType(ctx))
        )
                .distinct(ctx.isDistinct());
    }

    //To be called from ProjectionList  
    public Selection toSelection(QueryContext ctx) {
        Expression expression = toExpression(ctx);

        if (alias != null) {
            expression.alias(alias);
        }
        return expression;
    }

    public Projection as(String alias) {
        setAlias(alias);
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Class getReturnType(QueryContext ctx) {
        System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName());
        if (nestedProjection.isPresent()) {
            Class nestedExpressionReturnType = nestedProjection.get().getPathExpression().getReturnType(ctx);

            System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " nestedExpressionReturnType = " + nestedExpressionReturnType);
            Class resolvedType = pathExpression.getReturnTypeResolver().apply(nestedExpressionReturnType);
            System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " resolvedType = " + resolvedType);

            return resolvedType;
        }
        Class returnType = pathExpression.getReturnType(ctx);
        System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " returnType = " + returnType);

        return returnType;
    }

}
