/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.criterion.predicate.base;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public abstract class ComparationCriterion<T extends Comparable<T>> extends Criterion {

    private final T value;

    public ComparationCriterion(String path, T value) {
        super(path);
        this.value = value;
    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        return getPredicate(builder, joinMap.get(getJoinName()).<T>get(getPropertyName()), value);
    }

    public abstract Predicate getPredicate(CriteriaBuilder builder, Path<T> path, T value);

    protected T getValue() { //Used in toString
        return value;
    }
 
    
}
