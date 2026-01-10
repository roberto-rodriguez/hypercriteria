package io.hypercriteria;

import io.hypercriteria.base.Selectable;
import io.hypercriteria.criterion.projection.base.Projection;
import io.hypercriteria.criterion.projection.Sum;
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

    @Override
    public Criteria select() {
        return new Criteria(entityManager);
    }

    @Override
    public Criteria select(String property) {
        return new Criteria(entityManager, Projections.property(property));
    }

    public Criteria select(Class resultType) {
        return new Criteria(entityManager, resultType);
    }

    //------ Count -----------
    @Override
    public Criteria count() {
        return criteria(Projections.count());
    }

    @Override
    public Criteria count(String property) {
        return criteria(Projections.count(property));
    }

    @Override
    public Criteria countDistinct() {
        return criteria(Projections.countDistinct());
    }

    @Override
    public Criteria countDistinct(String property) {
        return criteria(Projections.countDistinct(property));
    }

    //------ Sum -----------
    @Override
    public Criteria sum(String property, Class<?> resultType) {
        return criteria(new Sum(property, resultType));
    }

    //-- Min/Max
    //TODO:: Infer type
    @Override
    public Criteria min(String property, Class resultType) {
        return new Criteria(entityManager, Projections.min(property, resultType));
    }

    //TODO:: Infer type
    @Override
    public Criteria max(String property, Class resultType) {
        return new Criteria(entityManager, Projections.max(property, resultType));
    }

    @Override
    public Criteria avg(String property) {
        return new Criteria(entityManager, Projections.avg(property));
    }

    @Override
    public Criteria abs(String property, Class resultType) {
        return new Criteria(entityManager, Projections.abs(property, resultType));
    }

    @Override
    public Criteria abs(SimpleProjection simpleProjection) {
        return new Criteria(entityManager, Projections.abs(simpleProjection));
    }

    // -------- Private ---------
    private Criteria criteria(Projection projection) {
        return new Criteria(entityManager, projection);
    }

}
