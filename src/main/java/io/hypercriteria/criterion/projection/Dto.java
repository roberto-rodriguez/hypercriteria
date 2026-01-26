package io.hypercriteria.criterion.projection;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.projection.base.Projection;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

/**
 *
 * @author rrodriguez
 * @param <T>
 */
public class Dto<T> extends Projection {

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
