/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.criterion.predicate.base;

import io.fluentcriteria.context.QueryContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public abstract class Junction extends BasePredicate {

    protected List<BasePredicate> predicates = new ArrayList<>();

    protected Junction(BasePredicate... predicate) {
        this.predicates.addAll(Arrays.asList(predicate));
    }

    protected Junction(List<BasePredicate> predicates) {
        this.predicates = predicates;
    }

    protected Junction(BasePredicate p1, BasePredicate p2) {
        this.predicates = Arrays.asList(p1, p2);
    }
    
    protected Predicate[] getPredicates(QueryContext ctx){
        return  predicates.stream()
                .map(p -> p.toPredicate(ctx))
                .toArray(Predicate[]::new);
    }
}
