/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate;

import io.hypercriteria.criterion.Criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public class NotEqual extends Criterion {

    private Object value;

    public NotEqual(String path, Object value) {
        super(path);
        this.value = value;
    }

    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        return builder.notEqual(joinMap.get(getJoinName()).get(getPropertyName()), value);
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s<>%s", path, value);
    }
}
