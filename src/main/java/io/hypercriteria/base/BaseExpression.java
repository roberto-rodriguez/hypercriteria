/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.base;

import io.hypercriteria.context.JoinNode;
import io.hypercriteria.context.PathExpression;
import io.hypercriteria.context.PathResolver;
import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.projection.base.Projection;
import java.util.Optional;
import java.util.function.Function;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;

/**
 *
 * @author rrodriguez
 */
public abstract class BaseExpression {

    protected PathExpression pathExpression;

    protected Optional<BaseExpression> nestedProjection = Optional.empty();

    //To be called for Expressions with nested expressions
    public BaseExpression() {
    }

    public BaseExpression(String fieldPath) {
        this.pathExpression = new PathExpression(fieldPath, Function.identity());
    }

    public BaseExpression(String fieldPath, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    public BaseExpression(BaseExpression nestedProjection) {
        this(nestedProjection, Function.identity());
    }

    public BaseExpression(BaseExpression nestedProjection, Function<Class<?>, Class<?>> returnTypeResolver) {
        this.nestedProjection = Optional.of(nestedProjection);

        String fieldPath = this.nestedProjection.get().pathExpression.getRawPath();

        this.pathExpression = new PathExpression(fieldPath, returnTypeResolver);
    }

    public Expression resolvePath(QueryContext ctx) {
        Expression expression;

        if (nestedProjection.isPresent()) {
            expression = nestedProjection.get().resolvePath(ctx);
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

    public Expression toExpression(QueryContext ctx) {
        Expression path;

        if (nestedProjection.isEmpty()) {
            path = resolvePath(ctx);
        } else {
            path = ((Projection) nestedProjection.get()).toExpression(ctx);
        }

        return build(ctx, path);
    }

    protected abstract Expression build(QueryContext ctx, Expression expression);

    public PathExpression getPathExpression() {
        return pathExpression;
    }

}
