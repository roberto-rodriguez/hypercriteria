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
import io.hypercriteria.util.NumericType;
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
    public Criteria select(String property) {
        return new Criteria(entityManager, Projections.property(property));
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

    public static PropertyProjection property(String propertyName) {
        return new PropertyProjection(propertyName);
    }

    public static PropertyProjection groupProperty(String propertyName) {
        PropertyProjection propertyProjection = new PropertyProjection(propertyName);
        propertyProjection.setGroupBy(true);
        return propertyProjection;
    }

    public static Count count() {
        return new Count();
    }

    public static Count count(String propertyName) {
        return new Count(propertyName);
    }

    public static CountDistinct countDistinct() {
        return new CountDistinct();
    }

    public static CountDistinct countDistinct(String propertyName) {
        return new CountDistinct(propertyName);
    }

    public static Sum sum(String propertyName, Class resultType) {
        NumericType numericType = NumericType.from(resultType);
        return new Sum(propertyName, numericType.getPromotionTypeWhenSuming());
    }

    public static Max max(String propertyName, Class resultType) {
        return new Max(propertyName, resultType);
    }

    public static Min min(String propertyName, Class resultType) {
        return new Min(propertyName, resultType);
    }

    public static Avg avg(String propertyName) {
        return new Avg(propertyName);
    }

    public static Abs abs(String propertyName, Class resultType) {
        return new Abs(propertyName, resultType);
    }

    public static Abs abs(SimpleProjection simpleProjection) {
        return new Abs(simpleProjection);
    }

}
