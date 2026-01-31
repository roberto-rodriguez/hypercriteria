package io.hypercriteria.criterion.predicate.base;
 
import io.hypercriteria.context.QueryContext;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public abstract class BasePredicate{

    public abstract Predicate toPredicate(QueryContext ctx);

}
