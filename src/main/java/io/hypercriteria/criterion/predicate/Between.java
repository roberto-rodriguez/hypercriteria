/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate;

import io.hypercriteria.criterion.Criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public class Between<T extends Comparable<T>> extends Criterion {

    private T value1;
    private T value2;

    public Between(String path, T value1, T value2) {
        super(path);
        this.value1 = value1;
        this.value2 = value2;
    }

    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        Expression<? extends T> exp = joinMap.get(getJoinName()).<T>get(getPropertyName());
        return builder.between(exp, value1, value2);
    }

    @Override
    public String toString() {
        return String.format("%s between %s and %s", path, value1, value2);
    } 

}
