package io.fluentcriteria.predicate.base;
 
import io.fluentcriteria.context.QueryContext;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public abstract class BasePredicate{

    public abstract Predicate toPredicate(QueryContext ctx);

}
