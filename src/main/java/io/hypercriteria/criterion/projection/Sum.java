/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.projection.base.Projection;
import io.hypercriteria.util.NumericType;
import static io.hypercriteria.util.NumericType.BYTE;
import static io.hypercriteria.util.NumericType.DOUBLE;
import static io.hypercriteria.util.NumericType.FLOAT;
import static io.hypercriteria.util.NumericType.INTEGER;
import static io.hypercriteria.util.NumericType.LONG;
import static io.hypercriteria.util.NumericType.SHORT;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 */
public class Sum extends Projection {

    public Sum(String fieldPath) {
        super(fieldPath, t -> NumericType.from(t).getPromotionTypeWhenSuming());
    }

    public Sum(Projection nestedProjection) {
        super(nestedProjection, t -> NumericType.from(t).getPromotionTypeWhenSuming());
    }

    @Override
    public Expression build(QueryContext ctx, Expression expression) {
        CriteriaBuilder criteriaBuilder = ctx.getCriteriaBuilder();
        NumericType numericType = NumericType.from(pathExpression.getJavaType(ctx));

        return switch (numericType) {
            case BYTE, SHORT, INTEGER, LONG ->
                criteriaBuilder.sumAsLong(expression);
            case FLOAT, DOUBLE ->
                criteriaBuilder.sumAsDouble(expression);
            default ->
                criteriaBuilder.sum(expression);
        };
    }

}
