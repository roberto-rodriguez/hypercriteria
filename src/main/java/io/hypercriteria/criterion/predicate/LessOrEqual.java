/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate;

import io.hypercriteria.criterion.predicate.base.ComparationCriterion;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class LessOrEqual<T extends Comparable<T>> extends ComparationCriterion<T> {

    public LessOrEqual(String path, T value) {
        super(path, value);
    }

    public Predicate getPredicate(CriteriaBuilder builder, Path<T> path, T value) {
        return builder.lessThanOrEqualTo(path, value);
    }

    @Override
    public String toString() {
        return String.format("%s<=%s", path, getValue());
    }
}
