/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.expression.base;
 
import io.fluentcriteria.context.JoinNode;
import io.fluentcriteria.context.PathExpression;
import io.fluentcriteria.context.PathResolver;
import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.Field;
import io.fluentcriteria.predicate.And;
import io.fluentcriteria.predicate.Equal;
import io.fluentcriteria.predicate.GreaterThan;
import io.fluentcriteria.predicate.base.BasePredicate;
import io.fluentcriteria.predicate.IsTrue;
import io.fluentcriteria.predicate.builder.PredicateBuilder;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Selection;

/**
 *
 * @author rrodriguez
 */
public abstract class BaseExpression implements PredicateBuilder<BasePredicate> {

    protected PathExpression pathExpression;

    protected Optional<BaseExpression> nestedExpression = Optional.empty();

    protected String alias;

    //To be called for Expressions with nested expressions
    public BaseExpression() {
    }

    public BaseExpression(String fieldPath) {
        this.pathExpression = new PathExpression(fieldPath, Function.identity());
    }

    public BaseExpression(String fieldPath, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    public BaseExpression(BaseExpression nestedExpression) {
        this(nestedExpression, Function.identity());
    }

    public BaseExpression(BaseExpression nestedExpression, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.nestedExpression = Optional.of(nestedExpression);

        String fieldPath = this.nestedExpression.get().getPathExpression().getRawPath();

        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    //To be called directly by Criteria (if it is not included within a ProjectionsList) 
    public void apply(QueryContext ctx, CriteriaQuery criteriaQuery) {
        criteriaQuery.select(
                toExpression(ctx).as(getReturnType(ctx))
        )
                .distinct(ctx.isDistinct());
    }

    public Expression toExpression(QueryContext ctx) {
        Expression path;

        if (nestedExpression.isEmpty()) {
            path = resolvePath(ctx);
        } else {
            path = ((BaseExpression) nestedExpression.get()).toExpression(ctx);
        }

        return build(ctx, path);
    }

    protected abstract Expression build(QueryContext ctx, Expression expression);

    //To be called from ProjectionList  
    public Selection toSelection(QueryContext ctx) {
        Expression expression = toExpression(ctx);

        if (alias != null) {
            expression.alias(alias);
        }
        return expression;
    }

    public BaseExpression as(String alias) {
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
        if (nestedExpression.isPresent()) {
            Class nestedExpressionReturnType = nestedExpression.get().getPathExpression().getReturnType(ctx);

            System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " nestedExpressionReturnType = " + nestedExpressionReturnType);
            Class resolvedType = pathExpression.getReturnTypeResolver().apply(nestedExpressionReturnType);
            System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " resolvedType = " + resolvedType);

            return resolvedType;
        }
        Class returnType = pathExpression.getReturnType(ctx);
        System.out.println("DEBUG:: Projection.getReturnType from " + this.getClass().getSimpleName() + " returnType = " + returnType);

        return returnType;
    }

    public Expression resolvePath(QueryContext ctx) {
        Expression expression;

        if (nestedExpression.isPresent()) {
            expression = nestedExpression.get().resolvePath(ctx);
        } else {
            expression = getJoin(ctx);

            Optional<String> terminal = pathExpression.getTerminal(ctx);

            if (terminal.isPresent()) {
                expression = ((From) expression).get(terminal.get());
            }
        }

        return expression;
    }

    public From getJoin(QueryContext ctx) {
        System.out.println("DEBUG :: BaseExpression:: getJoin from pathExpression = " + pathExpression);
        Optional<JoinNode> optionalJoinNode = Optional.ofNullable(PathResolver.resolvePath(ctx, pathExpression));

        boolean hasJoinNode = optionalJoinNode.isPresent();
        System.out.println("DEBUG :: BaseExpression:: getJoin - hasJoinNode = " + hasJoinNode);

        if (hasJoinNode) {
            System.out.println("DEBUG :: BaseExpression:: getJoin - optionalJoinNode = " + optionalJoinNode);
        }

        JoinNode joinNode = optionalJoinNode.orElse(ctx.getRootNode());

        System.out.println("DEBUG :: BaseExpression:: getJoin - joinNode = " + joinNode);

        return joinNode.toFrom(ctx);
    }

    public PathExpression getPathExpression() {
        return pathExpression;
    }

    //------- Predicate builder 
    @Override
    public IsTrue isTrue(BaseExpression expressionValue) {
        return new IsTrue(expressionValue);
    }

    @Override
    public IsTrue isTrueField(String fieldPath) {
        return new IsTrue(new Field(fieldPath));
    }

    //Greater Than
    @Override
    public <T extends Comparable<T>> GreaterThan greaterThan(T value) {
        return new GreaterThan<>(this, value);
    }

    @Override
    public GreaterThan greaterThan(BaseExpression expressionValue) {
        return new GreaterThan<>(this, expressionValue);
    }

    @Override
    public GreaterThan greaterThanField(String fieldPath) {
        return greaterThan(new Field(fieldPath));
    }

    //Equal
    @Override
    public Equal equal(Object value) {
        return new Equal(this, value);
    }

    @Override
    public Equal equal(BaseExpression expressionValue) {
        return new Equal(this, expressionValue);
    }

    @Override
    public Equal equalField(String fieldPath) {
        return equal(new Field(fieldPath));
    }

}
