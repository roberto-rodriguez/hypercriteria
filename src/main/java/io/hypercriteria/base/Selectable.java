package io.hypercriteria.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.criterion.projection.base.Projection;

/**
 *
 * @author rrodriguez
 */
public interface Selectable {

    public Criteria select();

    public Criteria select(String property);

    public Criteria select(Class resultType);

    public Criteria select(Projection projection);
}
