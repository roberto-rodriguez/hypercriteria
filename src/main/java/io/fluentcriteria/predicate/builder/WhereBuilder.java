package io.fluentcriteria.predicate.builder;

import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.BasePredicate;

/**
 *
 * @author rrodriguez
 */
public class WhereBuilder extends ExpressionPredicateBuilder {

    public WhereBuilder(Criteria criteria, BaseExpression leftExpression) {
        super(criteria, leftExpression);
    }

    @Override
    protected Criteria setPredicate(BasePredicate predicate) {
        return criteria.where(predicate);
    }

}
