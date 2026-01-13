package io.hypercriteria;

import io.hypercriteria.base.Selectable;
import io.hypercriteria.criterion.ProjectionList;
import io.hypercriteria.criterion.projection.Abs;
import io.hypercriteria.criterion.projection.Avg;
import io.hypercriteria.criterion.projection.Count;
import io.hypercriteria.criterion.projection.CountDistinct;
import io.hypercriteria.criterion.projection.Max;
import io.hypercriteria.criterion.projection.Min;
import io.hypercriteria.criterion.projection.base.Projection;
import io.hypercriteria.criterion.projection.Sum;
import io.hypercriteria.criterion.projection.base.PropertyProjection;
import io.hypercriteria.criterion.projection.base.SimpleProjection;
import javax.persistence.EntityManager;

/**
 *
 * @author rrodriguez
 */
public class HyperCriteria implements Selectable {

    private final EntityManager entityManager;

    private HyperCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static HyperCriteria using(EntityManager entityManager) {
        return new HyperCriteria(entityManager);
    }

//    ------- Selects ------------
    @Override
    public Criteria select() {
        return new Criteria(entityManager);
    }

    @Override
    public Criteria select(String fieldPath) {
        return new Criteria(entityManager, property(fieldPath));
    }

    @Override
    public Criteria select(Class resultType) {
        return new Criteria(entityManager, resultType);
    }

    @Override
    public Criteria select(Projection projection) {
        return new Criteria(entityManager, projection);
    }

    // -------- Projections ------------
    public static ProjectionList projectionList() {
        return new ProjectionList();
    }

    public static PropertyProjection property(String fieldPath) {
        return new PropertyProjection(fieldPath);
    }

    public static PropertyProjection groupProperty(String fieldPath) {
        PropertyProjection propertyProjection = new PropertyProjection(fieldPath);
        propertyProjection.setGroupBy(true);
        return propertyProjection;
    }

    public static Count count() {
        return new Count();
    }

    public static Count count(String fieldPath) {
        return new Count(fieldPath);
    }

    public static CountDistinct countDistinct() {
        return new CountDistinct();
    }

    public static CountDistinct countDistinct(String fieldPath) {
        return new CountDistinct(fieldPath);
    }

    public static Sum sum(String fieldPath) {
        return new Sum(fieldPath);
    }

    public static Sum sum(SimpleProjection simpleProjection) {
        return new Sum(simpleProjection);
    }

    public static Max max(String fieldPath) {
        return new Max(fieldPath);
    }

    public static Min min(String fieldPath) {
        return new Min(fieldPath);
    }

    public static Avg avg(String fieldPath) {
        return new Avg(fieldPath);
    }

    public static Abs abs(String fieldPath) {
        return new Abs(fieldPath);
    }

    public static Abs abs(SimpleProjection simpleProjection) {
        return new Abs(simpleProjection);
    }

}
