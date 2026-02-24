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
import io.fluentcriteria.predicate.Between;
import io.fluentcriteria.predicate.Equal;
import io.fluentcriteria.predicate.Ge;
import io.fluentcriteria.predicate.GreaterThan;
import io.fluentcriteria.predicate.GreaterThanOrEqualTo;
import io.fluentcriteria.predicate.Gt;
import io.fluentcriteria.predicate.In;
import io.fluentcriteria.predicate.IsEmpty;
import io.fluentcriteria.predicate.IsFalse;
import io.fluentcriteria.predicate.IsNotEmpty;
import io.fluentcriteria.predicate.IsNotNull;
import io.fluentcriteria.predicate.IsNull;
import io.fluentcriteria.predicate.base.BasePredicate;
import io.fluentcriteria.predicate.IsTrue;
import io.fluentcriteria.predicate.Le;
import io.fluentcriteria.predicate.LessThan;
import io.fluentcriteria.predicate.LessThanOrEqualTo;
import io.fluentcriteria.predicate.Like;
import io.fluentcriteria.predicate.Like.MatchMode;
import io.fluentcriteria.predicate.Lt;
import io.fluentcriteria.predicate.Not;
import io.fluentcriteria.predicate.NotEqual;
import io.fluentcriteria.predicate.NotLike;
import io.fluentcriteria.predicate.builder.PredicateBuilder;
import java.util.Arrays;
import java.util.Collection;
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
    //Between
    @Override
    public <T extends Comparable> Between between(T start, T end) {
        return new Between(this, start, end);
    }

    @Override
    public Between between(BaseExpression start, BaseExpression end) {
        return new Between(this, start, end);
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

    //Ge
    @Override
    public Ge ge(Number value) {
        return new Ge(this, value);
    }

    @Override
    public Ge ge(BaseExpression expressionValue) {
        return new Ge(this, expressionValue);
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

    //Less Than
    @Override
    public <T extends Comparable<T>> LessThan lessThan(T value) {
        return new LessThan<>(this, value);
    }

    @Override
    public LessThan lessThan(BaseExpression expressionValue) {
        return new LessThan<>(this, expressionValue);
    }

    @Override
    public LessThan lessThanField(String fieldPath) {
        return lessThan(new Field(fieldPath));
    }

    //GreaterThanOrEqualTo
    @Override
    public <T extends Comparable<T>> GreaterThanOrEqualTo greaterThanOrEqualTo(T value) {
        return new GreaterThanOrEqualTo<>(this, value);
    }

    @Override
    public GreaterThanOrEqualTo greaterThanOrEqualTo(BaseExpression expressionValue) {
        return new GreaterThanOrEqualTo<>(this, expressionValue);
    }

    @Override
    public GreaterThanOrEqualTo greaterThanOrEqualToField(String fieldPath) {
        return greaterThanOrEqualTo(new Field(fieldPath));
    }

    //LessThanOrEqualTo
    @Override
    public <T extends Comparable<T>> LessThanOrEqualTo lessThanOrEqualTo(T value) {
        return new LessThanOrEqualTo<>(this, value);
    }

    @Override
    public LessThanOrEqualTo lessThanOrEqualTo(BaseExpression expressionValue) {
        return new LessThanOrEqualTo<>(this, expressionValue);
    }

    @Override
    public LessThanOrEqualTo lessThanOrEqualToField(String fieldPath) {
        return lessThanOrEqualTo(new Field(fieldPath));
    }

    //Lt
    @Override
    public Lt lt(Number value) {
        return new Lt(this, value);
    }

    @Override
    public Lt lt(BaseExpression expressionValue) {
        return new Lt(this, expressionValue);
    }

    //Gt
    @Override
    public Gt gt(Number value) {
        return new Gt(this, value);
    }

    @Override
    public Gt gt(BaseExpression expressionValue) {
        return new Gt(this, expressionValue);
    }

    // In
    @Override
    public In in(Collection collection) {
        return new In(this, collection);
    }

    @Override
    public In in(Object... values) {
        return new In(this, Arrays.asList(values));
    }

    @Override
    public In in(BaseExpression expressionValue) {
        return new In(this, Arrays.asList(expressionValue));
    }

    @Override
    public In in(BaseExpression... expressionValues) {
        return new In(this, Arrays.asList(expressionValues));
    }

    //IsEmpty 
    @Override
    public IsEmpty isEmpty() {
        return new IsEmpty(this);
    }

    //IsFalse 
    @Override
    public IsFalse isFalse() {
        return new IsFalse(this);
    }

    //IsNotEmpty 
    @Override
    public IsNotEmpty isNotEmpty() {
        return new IsNotEmpty(this);
    }

    //isNotNull 
    @Override
    public IsNotNull isNotNull() {
        return new IsNotNull(this);
    }

    //isNull 
    @Override
    public IsNull isNull() {
        return new IsNull(this);
    }

    //IsTrue 
    @Override
    public IsTrue isTrue(BaseExpression expressionValue) {
        return new IsTrue(expressionValue);
    }

    @Override
    public IsTrue isTrueField(String fieldPath) {
        return new IsTrue(new Field(fieldPath));
    }

    @Override
    public Equal equalField(String fieldPath) {
        return equal(new Field(fieldPath));
    }

    //Le
    @Override
    public Le le(Number value) {
        return new Le(this, value);
    }

    @Override
    public Le le(BaseExpression expressionValue) {
        return new Le(this, expressionValue);
    }

    //Like
    @Override
    public Like like(String value) {
        return new Like(this, value);
    }

    @Override
    public Like like(String value, Like.MatchMode matchMode) {
        return new Like(this, value, matchMode);
    }

    @Override
    public Like likeField(String fieldPath) {
        return like(new Field(fieldPath));
    }

    @Override
    public Like like(String value, char escapeChar) {
        return new Like(this, value, escapeChar);
    }

    @Override
    public Like like(String value, BaseExpression escapeCharExpression) {
        return new Like(this, value, escapeCharExpression);
    }

    @Override
    public Like like(BaseExpression expression) {
        return new Like(this, expression);
    }

    @Override
    public Like like(BaseExpression expression, char escapeChar) {
        return new Like(this, expression, escapeChar);
    }

    @Override
    public Like like(BaseExpression expression, BaseExpression escapeCharExpression) {
        return new Like(this, expression, escapeCharExpression);
    }

    //NotLike
    @Override
    public NotLike notLike(String value) {
        return new NotLike(this, value);
    }

    @Override
    public NotLike notLike(String value, MatchMode matchMode) {
        return new NotLike(this, value, matchMode);
    }

    @Override
    public NotLike notLikeField(String fieldPath) {
        return notLike(new Field(fieldPath));
    }

    @Override
    public NotLike notLike(String value, char escapeChar) {
        return new NotLike(this, value, escapeChar);
    }

    @Override
    public NotLike notLike(String value, BaseExpression escapeCharExpression) {
        return new NotLike(this, value, escapeCharExpression);
    }

    @Override
    public NotLike notLike(BaseExpression expression) {
        return new NotLike(this, expression);
    }

    @Override
    public NotLike notLike(BaseExpression expression, char escapeChar) {
        return new NotLike(this, expression, escapeChar);
    }

    @Override
    public NotLike notLike(BaseExpression expression, BaseExpression escapeCharExpression) {
        return new NotLike(this, expression, escapeCharExpression);
    }

    @Override
    public Not not(BaseExpression expressionValue) {
        return new Not(this);
    }

    //Not Equal
    @Override
    public NotEqual notEqual(Object value) {
        return new NotEqual(this, value);
    }

    @Override
    public NotEqual notEqual(BaseExpression expressionValue) {
        return new NotEqual(this, expressionValue);
    }

}
