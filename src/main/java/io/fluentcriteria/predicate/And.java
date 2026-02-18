package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.predicate.base.BasePredicate;
import io.fluentcriteria.predicate.base.Junction;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class And extends Junction {

    public And(BasePredicate p1, BasePredicate p2) {
        this.predicates = Arrays.asList(p1, p2);
    }

    public And(BasePredicate... predicate) {
        this.predicates.addAll(Arrays.asList(predicate));
    }

    public And(List<BasePredicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        return ctx.getCriteriaBuilder().and(getPredicates(ctx));
    }

}
