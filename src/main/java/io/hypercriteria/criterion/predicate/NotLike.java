/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate;

import io.hypercriteria.criterion.Criterion;
import io.hypercriteria.criterion.MatchMode;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public class NotLike extends Criterion {

    private Object value;
    private MatchMode matchMode = MatchMode.EXACT;

    public NotLike(String path, Object value) {
        super(path);
        this.value = value;
    }

    public NotLike(String path, Object value, MatchMode matchMode) {
        this(path, value);
        this.matchMode = matchMode;
    }

    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        return builder.notLike(joinMap.get(getJoinName()).get(getPropertyName()), matchMode.toMatchString(value));
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s ilike %s", path, matchMode.toMatchString(value));
    }
}
