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
public class Like extends Criterion {

    private Object value;
    private MatchMode matchMode = MatchMode.EXACT;

    public Like(String path, Object value) {
        super(path);
        this.value = value;
    }

    public Like(String path, Object value, MatchMode matchMode) {
        this(path, value);
        this.matchMode = matchMode;
    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        String join = getJoinName();

        if (!joinMap.containsKey(join)) {
            throw new IllegalArgumentException("Alias " + join + " is not declared.");
        }

        return builder.like(joinMap.get(join).get(getPropertyName()), matchMode.toMatchString(value));
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s like %s", path, matchMode.toMatchString(value));
    }
}
