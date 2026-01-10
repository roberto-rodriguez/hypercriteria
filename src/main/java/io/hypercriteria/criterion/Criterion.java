/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public abstract class Criterion {
    
    protected String path; // Used in toString()
    protected String joinName = "";
    protected String propertyName;

    public Criterion() {
    }

    public Criterion(String path) {
        this.path = path;
        if (path.contains(".")) {
            String[] parts = path.split("\\.");
            this.joinName = parts[0];
            this.propertyName = parts[1];
        } else {
            this.propertyName = path;
        }
    }

    public abstract Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap);

    /**
     * @return the alias
     */
    public String getJoinName() {
        return joinName;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

}
