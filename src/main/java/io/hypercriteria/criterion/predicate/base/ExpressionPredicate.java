package io.hypercriteria.criterion.predicate.base;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.expression.Property;
import io.hypercriteria.criterion.expression.base.BaseExpression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class ExpressionPredicate extends BasePredicate {

    private BaseExpression expression;

    public ExpressionPredicate(String fieldPath) {
        this.expression
                = new Property(fieldPath);
    }

//    public ExpressionPredicate(Projection expression) {
//        this.expression
//                = new Property(fieldPath);
//    }

    @Override
    public Predicate toPredicate(QueryContext ctx) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
