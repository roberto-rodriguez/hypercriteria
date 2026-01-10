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
public class Not extends Criterion {

    private Criterion expression = null;

    public Not(Criterion expression) {
        this.expression = expression;
    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        return builder.not(expression.getPredicate(builder, joinMap));
    }

    @Override
    public String toString() {
        return String.format("not %s", expression.toString());
    }

}
