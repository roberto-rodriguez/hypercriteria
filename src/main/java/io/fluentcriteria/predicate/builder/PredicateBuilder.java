package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.Like.MatchMode;
import java.util.Collection;

/**
 *
 * @author rrodriguez
 * @param <R>
 */
public interface PredicateBuilder<R> {

    //Between
    public <T extends Comparable> R between(T start, T end);

    public R between(BaseExpression start, BaseExpression end);

    //Equal 
    public R equal(Object value);

    public R equal(BaseExpression expression);

    public R equalField(String fieldPath);

    // Ge
    public R ge(Number value);

    public R ge(BaseExpression expression);

    //Greater Than
    public <T extends Comparable<T>> R greaterThan(T value);

    public R greaterThan(BaseExpression expression);

    public R greaterThanField(String fieldPath);

    //Less Than
    public <T extends Comparable<T>> R lessThan(T value);

    public R lessThan(BaseExpression expression);

    public R lessThanField(String fieldPath);

    //greaterThanOrEqualTo
    public <T extends Comparable<T>> R greaterThanOrEqualTo(T value);

    public R greaterThanOrEqualTo(BaseExpression expression);

    public R greaterThanOrEqualToField(String fieldPath);

    //lessThanOrEqualTo
    public <T extends Comparable<T>> R lessThanOrEqualTo(T value);

    public R lessThanOrEqualTo(BaseExpression expression);

    public R lessThanOrEqualToField(String fieldPath);

    // In
    public R in(Collection collection);

    public R in(BaseExpression expression);

    public R in(BaseExpression... expressions);

    public R in(Object... objects);

    // IsEmpty
    public R isEmpty();

    // IsFalse
    public R isFalse();

    // IsEmpty
    public R isNotEmpty();

    // IsNotNull
    public R isNotNull();

    // IsNull
    public R isNull();

    // isTrue
    public R isTrue(BaseExpression expression);

    public R isTrueField(String fieldPath);

    // Lt
    public R lt(Number value);

    public R lt(BaseExpression expression);

    // Gt
    public R gt(Number value);

    public R gt(BaseExpression expression);

    // Le
    public R le(Number value);

    public R le(BaseExpression expression);

    // Like
    public R like(String value);

    public R like(String value, MatchMode matchMode);

    public R likeField(String fieldPath);

    public R like(String value, char escapeChar);

    public R like(String value, BaseExpression escapeCharExpression);

    public R like(BaseExpression expression);

    public R like(BaseExpression expression, char escapeChar);

    public R like(BaseExpression expression, BaseExpression escapeCharExpression);

    // Not Like
    public R notLike(String value);

    public R notLike(String value, MatchMode matchMode);

    public R notLikeField(String fieldPath);

    public R notLike(String value, char escapeChar);

    public R notLike(String value, BaseExpression escapeCharExpression);

    public R notLike(BaseExpression expression);

    public R notLike(BaseExpression expression, char escapeChar);

    public R notLike(BaseExpression expression, BaseExpression escapeCharExpression);

    // Not
    public R not(BaseExpression expression);

    //Not Equal
    public R notEqual(Object value);

    public R notEqual(BaseExpression expression);
}
