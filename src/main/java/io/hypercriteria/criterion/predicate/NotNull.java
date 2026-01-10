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
public class NotNull extends Criterion {

    public NotNull(String path) {
        super(path);
    }

    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        return builder.isNotNull(joinMap.get(getJoinName()).get(getPropertyName()));
    }

    @Override
    public String toString() {
        return String.format("%s is not null", path);
    }
}
