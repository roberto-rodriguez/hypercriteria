package io.hypercriteria.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.criterion.expression.base.BaseExpression;

/**
 *
 * @author rrodriguez
 */
public interface Selectable {

    public Criteria select();

    public Criteria select(String attribute);

    public Criteria select(Class resultType);

    public Criteria select(BaseExpression projection);
}
