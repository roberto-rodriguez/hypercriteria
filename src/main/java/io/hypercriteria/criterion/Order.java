/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public class Order {

    enum Type {

        ASC, DESC
    }

    private String alias = "";
    private String path;
    private String propertyName;
    private Type type;

    public Order(String path, Type type) {
        this.type = type;
        this.path = path;

        if (path.contains(".")) {
            String[] parts = path.split("\\.");
            this.alias = parts[0];
            this.propertyName = parts[1];
        } else {
            this.propertyName = path;
        }
    }

    public static Order asc(String propertyName) {
        return new Order(propertyName, Type.ASC);
    }

    public static Order desc(String propertyName) {
        return new Order(propertyName, Type.DESC);
    }

    public javax.persistence.criteria.Order build(CriteriaBuilder builder, Map<String, From> joinMap) {
        if (this.type == Type.ASC) {
            return builder.asc(getExpression(joinMap));
        } else {
            return builder.desc(getExpression(joinMap));
        }
    }

    private Expression getExpression(Map<String, From> joinMap) {
        return joinMap.get(alias).get(propertyName);
    }

    @Override
    public String toString() {
        return String.format("%s %s", path, type.toString().toLowerCase());
    } 
}
