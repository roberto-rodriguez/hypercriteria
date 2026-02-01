package io.fluentcriteria.expression;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public class Dto<T> extends BaseExpression {

    private final Class<T> dtoType;

    public Dto(Class<T> dtoType) {
        this.dtoType = dtoType;
    }

    @Override
    public void apply(QueryContext ctx, CriteriaQuery criteriaQuery) {

    }

    @Override
    protected Expression build(QueryContext ctx, Expression expression) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Class getReturnType(QueryContext ctx) {
        return dtoType;
    }

}
