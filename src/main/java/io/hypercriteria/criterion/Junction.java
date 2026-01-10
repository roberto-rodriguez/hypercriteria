/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public abstract class Junction extends Criterion {

    protected final List<Criterion> conditions = new ArrayList<>();

    protected Junction() {
    }

    protected Junction(Criterion... criterion) {
        this.conditions.addAll(Arrays.asList(criterion));
    }

    public Junction add(Criterion criterion) {
        this.conditions.add(criterion);
        return this;
    }
    
    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        Predicate[] predicates = conditions.stream()
                .map(c -> c.getPredicate(builder, joinMap))
                .toArray(Predicate[]::new);
        return getPredicate(builder, predicates);
    }
    
    protected abstract Predicate getPredicate(CriteriaBuilder builder, Predicate[] predicates);

}
