package io.fluentcriteria.base;

import io.fluentcriteria.Criteria;
import io.fluentcriteria.expression.base.BaseExpression;

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
