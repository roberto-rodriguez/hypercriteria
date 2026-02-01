package io.fluentcriteria.criterion.predicate;

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
public class Or extends Junction {

    public Or(BasePredicate... predicate) {
        this.predicates.addAll(Arrays.asList(predicate));
    }

    public Or(List<BasePredicate> predicates) {
        this.predicates = predicates;
    }

    public Or(BasePredicate p1, BasePredicate p2) {
        this.predicates = Arrays.asList(p1, p2);
    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        return ctx.getCriteriaBuilder().or(getPredicates(ctx));
    }

}
