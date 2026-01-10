/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

import io.hypercriteria.criterion.predicate.Between;
import io.hypercriteria.criterion.predicate.Equal;
import io.hypercriteria.criterion.predicate.GreaterOrEqual;
import io.hypercriteria.criterion.predicate.GreaterThan;
import io.hypercriteria.criterion.predicate.In;
import io.hypercriteria.criterion.predicate.IsNull;
import io.hypercriteria.criterion.predicate.LessOrEqual;
import io.hypercriteria.criterion.predicate.LessThan;
import io.hypercriteria.criterion.predicate.Like;
import io.hypercriteria.criterion.predicate.Not;
import io.hypercriteria.criterion.predicate.NotEqual;
import io.hypercriteria.criterion.predicate.NotLike;
import io.hypercriteria.criterion.predicate.NotNull;

import java.util.List;

/**
 *
 * @author rrodriguez
 */
public class Restrictions {

    // Equality
    public static Equal eq(String path, Object value) {
        return new Equal(path, value);
    }

    public static NotEqual ne(String path, Object value) {
        return new NotEqual(path, value);
    }

    public static Like like(String path, String value) {
        return new Like(path, value);
    }

    public static Like like(String path, String value, MatchMode matchMode) {
        return new Like(path, value, matchMode);
    }

    public static NotLike ilike(String path, String value) {
        return new NotLike(path, value);
    }

    public static NotLike ilike(String path, String value, MatchMode matchMode) {
        return new NotLike(path, value, matchMode);
    }

    // Null
    public static IsNull isNull(String path) {
        return new IsNull(path);
    }
    
    public static NotNull isNotNull(String path) {
        return new NotNull(path);
    }

    // Comparison
    public static <T extends Comparable<T>> GreaterThan gt(String path, T value) {
        return new GreaterThan<T>(path, value);
    }

    public static <T extends Comparable<T>> LessThan lt(String path, T value) {
        return new LessThan<T>(path, value);
    }

    public static <T extends Comparable<T>> GreaterOrEqual ge(String path, T value) {
        return new GreaterOrEqual<T>(path, value);
    }

    public static <T extends Comparable<T>> LessOrEqual le(String path, T value) {
        return new LessOrEqual<T>(path, value);
    }

    // Junctions 
    public static Conjunction and(Criterion... predicates) {
        return new Conjunction(predicates);
    }

    public static Disjunction or(Criterion... predicates) {
        return new Disjunction(predicates);
    }

    // Inclussion
    //TODO make this generic and accept ...
    public static <T> In in(String path, List<T> list) {
        return new In(path, list);
    }

    public static <T extends Comparable<T>> Between between(String path, T from, T to) {
        return new Between(path, from, to);
    }

    //Negation
    public static Not not(Criterion criterion) {
        return new Not(criterion);
    }

}
