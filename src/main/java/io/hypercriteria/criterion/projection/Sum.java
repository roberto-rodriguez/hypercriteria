/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria.criterion.projection;

import io.hypercriteria.criterion.projection.base.SimpleProjection;
import io.hypercriteria.criterion.projection.base.TypedSimpleProjection;
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
public class Sum extends TypedSimpleProjection {

    public Sum(String fieldPath) {
        super(fieldPath);
    }

    public Sum(SimpleProjection nestedProjection) {
        super(nestedProjection);
    }

    @Override
    public Expression build(CriteriaBuilder builder, Expression expression) {
        NumericType numericType = NumericType.from(getReturnType().get());

        return switch (numericType) {
            case BYTE, SHORT, INTEGER, LONG ->
                builder.sumAsLong(expression);
            case FLOAT, DOUBLE ->
                builder.sumAsDouble(expression);
            default ->
                builder.sum(expression);
        };
    }

    @Override
    protected void updateReturnType() {
        NumericType numericType = NumericType.from(pathInfo.getJavaType());
        pathInfo.setJavaType(numericType.getPromotionTypeWhenSuming());
    }
}
