package io.fluentcriteria.predicate.base;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.predicate.And;
import io.fluentcriteria.predicate.Or;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public abstract class BasePredicate {

    public abstract Predicate toPredicate(QueryContext ctx);

    // -- Junctions
    public And and(BasePredicate p2) {
        return new And(this, p2);
    }

    public Or or(BasePredicate p2) {
        return new Or(this, p2);
    }

}
