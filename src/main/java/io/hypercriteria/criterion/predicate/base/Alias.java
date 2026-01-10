/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate.base;

import javax.persistence.criteria.JoinType;

import javax.persistence.criteria.From;
import java.util.Map;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;

/**
 *
 * @author rrodriguez
 */
public class Alias {

    // name can be rootName.joinName or joinName
    private String name;
    private String alias;
    private JoinType joinType;

    public Alias(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public Alias(String name, String alias, JoinType joinType) {
        this(name, alias);
        this.joinType = joinType;
    }

    public void join(Map<String, From> joinMap) {
        From root = joinMap.get("");
        String joinName = name;
        String rootName = "";

        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            rootName = parts[0];
            joinName = parts[1];

            root = joinMap.get(rootName);
        }

        if (root == null) {
            throw new IllegalArgumentException(String.format("Unable to get root from rootName: %s and name: %s", rootName, name));
        }

        From join;

        if (joinType == null) {
            join = root.join(joinName);
        } else {
            join = root.join(joinName, joinType);
        }

        joinMap.put(alias, join);
    }

    public void fetch(Map<String, FetchParent> fetchMap) {
        FetchParent root = fetchMap.get("");
        String joinName = name;
        String rootName = "";

        if (name.contains(".")) {
            String[] parts = name.split("\\.");
            rootName = parts[0];
            joinName = parts[1];

            root = fetchMap.get(rootName);
        }

        if (root == null) {
            throw new IllegalArgumentException(String.format("Unable to get root from rootName: %s and name: %s", rootName, name));
        }

        Fetch fetch;

        if (joinType == null) {
            fetch = root.fetch(joinName);
        } else {
            fetch = root.fetch(joinName, joinType);
        }

        fetchMap.put(alias, fetch);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return the joinType
     */
    public JoinType getJoinType() {
        return joinType;
    }

}
