package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.predicate.IsTrue;
import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.Field;
import io.fluentcriteria.expression.base.BaseExpression;
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
import io.fluentcriteria.predicate.Le;
import io.fluentcriteria.predicate.LessThan;
import io.fluentcriteria.predicate.LessThanOrEqualTo;
import io.fluentcriteria.predicate.Like;
import io.fluentcriteria.predicate.Like.MatchMode;
import io.fluentcriteria.predicate.Lt;
import io.fluentcriteria.predicate.Not;
import io.fluentcriteria.predicate.NotEqual;
import io.fluentcriteria.predicate.NotLike;
import io.fluentcriteria.predicate.base.BasePredicate;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author rrodriguez
 */
public abstract class CriteriaPredicateBuilder implements PredicateBuilder<Criteria> {

    protected Criteria criteria;
    protected BaseExpression leftExpression;

    public CriteriaPredicateBuilder(Criteria criteria, BaseExpression leftExpression) {
        this.criteria = criteria;
        this.leftExpression = leftExpression;
    }

    protected abstract Criteria setPredicate(BasePredicate predicate);

    //Between
    @Override
    public <T extends Comparable> Criteria between(T start, T end) {
        return setPredicate(new Between(leftExpression, start, end));
    }

    @Override
    public Criteria between(BaseExpression start, BaseExpression end) {
        return setPredicate(new Between(leftExpression, start, end));
    }

    //Equal
    @Override
    public Criteria equal(Object value) {
        return setPredicate(new Equal(leftExpression, value));
    }

    @Override
    public Criteria equal(BaseExpression expressionValue) {
        return setPredicate(new Equal(leftExpression, expressionValue));
    }

    @Override
    public Criteria equalField(String fieldPath) {
        return equal(new Field(fieldPath));
    }

    //Ge
    @Override
    public Criteria ge(Number value) {
        return setPredicate(new Ge(leftExpression, value));
    }

    @Override
    public Criteria ge(BaseExpression expressionValue) {
        return setPredicate(new Ge(leftExpression, expressionValue));
    }

    //Greater Than
    @Override
    public <T extends Comparable<T>> Criteria greaterThan(T value) {
        return setPredicate(new GreaterThan<>(leftExpression, value));
    }

    @Override
    public Criteria greaterThan(BaseExpression expressionValue) {
        return setPredicate(new GreaterThan<>(leftExpression, expressionValue));
    }

    @Override
    public Criteria greaterThanField(String fieldPath) {
        return greaterThan(new Field(fieldPath));
    }

    //Less Than
    @Override
    public <T extends Comparable<T>> Criteria lessThan(T value) {
        return setPredicate(new LessThan<>(leftExpression, value));
    }

    @Override
    public Criteria lessThan(BaseExpression expressionValue) {
        return setPredicate(new LessThan<>(leftExpression, expressionValue));
    }

    @Override
    public Criteria lessThanField(String fieldPath) {
        return lessThan(new Field(fieldPath));
    }

    //GreaterThanOrEqualTo
    @Override
    public <T extends Comparable<T>> Criteria greaterThanOrEqualTo(T value) {
        return setPredicate(new GreaterThanOrEqualTo<>(leftExpression, value));
    }

    @Override
    public Criteria greaterThanOrEqualTo(BaseExpression expressionValue) {
        return setPredicate(new GreaterThanOrEqualTo<>(leftExpression, expressionValue));
    }

    @Override
    public Criteria greaterThanOrEqualToField(String fieldPath) {
        return greaterThanOrEqualTo(new Field(fieldPath));
    }

    //LessThanOrEqualTo
    @Override
    public <T extends Comparable<T>> Criteria lessThanOrEqualTo(T value) {
        return setPredicate(new LessThanOrEqualTo<>(leftExpression, value));
    }

    @Override
    public Criteria lessThanOrEqualTo(BaseExpression expressionValue) {
        return setPredicate(new LessThanOrEqualTo<>(leftExpression, expressionValue));
    }

    @Override
    public Criteria lessThanOrEqualToField(String fieldPath) {
        return lessThanOrEqualTo(new Field(fieldPath));
    }

    //Ge
    @Override
    public Criteria lt(Number value) {
        return setPredicate(new Lt(leftExpression, value));
    }

    @Override
    public Criteria lt(BaseExpression expressionValue) {
        return setPredicate(new Lt(leftExpression, expressionValue));
    }

    //Ge
    @Override
    public Criteria gt(Number value) {
        return setPredicate(new Gt(leftExpression, value));
    }

    @Override
    public Criteria gt(BaseExpression expressionValue) {
        return setPredicate(new Gt(leftExpression, expressionValue));
    }

    //In
    @Override
    public Criteria in(Collection collection) {
        return setPredicate(new In(leftExpression, collection));
    }

    @Override
    public Criteria in(Object... values) {
        return setPredicate(new In(leftExpression, Arrays.asList(values)));
    }

    @Override
    public Criteria in(BaseExpression expressionValue) {
        return setPredicate(new In(leftExpression, Arrays.asList(expressionValue)));
    }

    @Override
    public Criteria in(BaseExpression... expressionValues) {
        return setPredicate(new In(leftExpression, Arrays.asList(expressionValues)));
    }

    //Is Empty
    @Override
    public Criteria isEmpty() {
        return setPredicate(new IsEmpty(leftExpression));
    }

    //IsFalse
    @Override
    public Criteria isFalse() {
        return setPredicate(new IsFalse(leftExpression));
    }

    //IsNotEmpty
    @Override
    public Criteria isNotEmpty() {
        return setPredicate(new IsNotEmpty(leftExpression));
    }

    //isNotNull
    @Override
    public Criteria isNotNull() {
        return setPredicate(new IsNotNull(leftExpression));
    }

    //isNull
    @Override
    public Criteria isNull() {
        return setPredicate(new IsNull(leftExpression));
    }

    //isTrue
    @Override
    public Criteria isTrue(BaseExpression expressionValue) {
        return setPredicate(new IsTrue(leftExpression));
    }

    @Override
    public Criteria isTrueField(String fieldPath) {
        return setPredicate(new IsTrue(new Field(fieldPath)));
    }

    //Ge
    @Override
    public Criteria le(Number value) {
        return setPredicate(new Le(leftExpression, value));
    }

    @Override
    public Criteria le(BaseExpression expressionValue) {
        return setPredicate(new Le(leftExpression, expressionValue));
    }

    //Like
    @Override
    public Criteria like(String value) {
        return setPredicate(new Like(leftExpression, value));
    }

    @Override
    public Criteria like(String value, MatchMode matchMode) {
        return setPredicate(new Like(leftExpression, value, matchMode));
    }

    @Override
    public Criteria likeField(String fieldPath) {
        return like(new Field(fieldPath));
    }

    @Override
    public Criteria like(String value, char escapeChar) {
        return setPredicate(new Like(leftExpression, value, escapeChar));
    }

    @Override
    public Criteria like(String value, BaseExpression escapeCharExpression) {
        return setPredicate(new Like(leftExpression, value, escapeCharExpression));
    }

    @Override
    public Criteria like(BaseExpression expression) {
        return setPredicate(new Like(leftExpression, expression));
    }

    @Override
    public Criteria like(BaseExpression expression, char escapeChar) {
        return setPredicate(new Like(leftExpression, expression, escapeChar));
    }

    @Override
    public Criteria like(BaseExpression expression, BaseExpression escapeCharExpression) {
        return setPredicate(new Like(leftExpression, expression, escapeCharExpression));
    }

    //NotLike
    @Override
    public Criteria notLike(String value) {
        return setPredicate(new NotLike(leftExpression, value));
    }

    @Override
    public Criteria notLike(String value, MatchMode matchMode) {
        return setPredicate(new NotLike(leftExpression, value, matchMode));
    }

    @Override
    public Criteria notLikeField(String fieldPath) {
        return notLike(new Field(fieldPath));
    }

    @Override
    public Criteria notLike(String value, char escapeChar) {
        return setPredicate(new NotLike(leftExpression, value, escapeChar));
    }

    @Override
    public Criteria notLike(String value, BaseExpression escapeCharExpression) {
        return setPredicate(new NotLike(leftExpression, value, escapeCharExpression));
    }

    @Override
    public Criteria notLike(BaseExpression expression) {
        return setPredicate(new NotLike(leftExpression, expression));
    }

    @Override
    public Criteria notLike(BaseExpression expression, char escapeChar) {
        return setPredicate(new NotLike(leftExpression, expression, escapeChar));
    }

    @Override
    public Criteria notLike(BaseExpression expression, BaseExpression escapeCharExpression) {
        return setPredicate(new NotLike(leftExpression, expression, escapeCharExpression));
    }
//Not  

    @Override
    public Criteria not(BaseExpression expression) {
        return setPredicate(new Not(expression));
    }

    //Not Equal
    @Override
    public Criteria notEqual(Object value) {
        return setPredicate(new NotEqual(leftExpression, value));
    }

    @Override
    public Criteria notEqual(BaseExpression expressionValue) {
        return setPredicate(new NotEqual(leftExpression, expressionValue));
    }

}
