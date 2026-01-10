/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection.base;

import io.hypercriteria.Criteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author rrodriguez
 */
public interface Projection {

    public void apply(Criteria criteria, CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);

    public Optional<Class> getReturnType();

    public void applyGroupBy(CriteriaBuilder builder, CriteriaQuery query, Map<String, From> joinMap);
}
