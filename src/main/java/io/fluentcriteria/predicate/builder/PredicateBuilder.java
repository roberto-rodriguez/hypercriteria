package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 * @param <R>
 */
public interface PredicateBuilder<R> {

    public R isTrue(BaseExpression expressionValue);

    public R isTrueField(String fieldPath);

    //Greater Than
    public <T extends Comparable<T>> R greaterThan(T value);

    public R greaterThan(BaseExpression expressionValue);

    public R greaterThanField(String fieldPath);

    //Equal
    public R equal(Object value);

    public R equal(BaseExpression expressionValue);

    public R equalField(String fieldPath);
}
