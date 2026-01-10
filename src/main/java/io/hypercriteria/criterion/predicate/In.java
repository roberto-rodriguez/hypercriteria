/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.predicate;
 
import io.hypercriteria.criterion.Criterion;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author rrodriguez
 */
public class In<T> extends Criterion {

    private List<T> values = new ArrayList<>();

//    public RestrictionIn(String path, Collection values) {
//        super(path);
//        this.values = Lists.newArrayList(values);
////        this.values.addAll(values);
//    }

    public In(String path, List<T> values) {
        super(path);
        this.values = new ArrayList(values); 
    }
 
//
//    public RestrictionIn(String path, Object... values) {
//        super(path);
//        for (Object value : values) {
//            this.values = Lists.newArrayList((List<Integer>)value);//.add((Integer) value);
//        } 
//    }

    @Override
    public Predicate getPredicate(CriteriaBuilder builder, Map<String, From> joinMap) {
        Expression<T> expression = joinMap.<T>get(joinName).get(propertyName);
        return expression.in(values);
    }

    @Override
    public String toString() {
        return String.format("%s in %s", path, values.stream().map(String::valueOf).collect(Collectors.joining(", ", "(", ")")));
    }

}
