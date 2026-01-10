package io.hypercriteria.base;

import io.hypercriteria.Criteria;
import io.hypercriteria.criterion.projection.base.SimpleProjection;

/**
 *
 * @author rrodriguez
 */
public interface Selectable {

    public Criteria select();

    public Criteria select(String property);

    public Criteria count();

    public Criteria count(String property);

    public Criteria countDistinct();

    public Criteria countDistinct(String property);

    public Criteria sum(String property, Class<?> returnType);

    public Criteria min(String property, Class resultType);

    public Criteria max(String property, Class resultType);

    public Criteria avg(String property);

    public Criteria abs(String property, Class resultType);

    public Criteria abs(SimpleProjection simpleProjection);
}
