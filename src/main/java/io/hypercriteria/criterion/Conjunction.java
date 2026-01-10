/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author rrodriguez
 */
public class Conjunction extends Junction {

    protected Conjunction() {
    }

    protected Conjunction(Criterion... criterion) {
        super(criterion);
    }

    @Override
    protected Predicate getPredicate(CriteriaBuilder builder, Predicate[] predicates) {
        return builder.and(predicates);
    }

    @Override
    public String toString() {
        return conditions.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining(" and ", "(", ")"));
    }

}
