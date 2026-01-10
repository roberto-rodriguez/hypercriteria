package io.hypercriteria;

import io.hypercriteria.base.Selectable;
import io.hypercriteria.criterion.projection.base.SimpleProjection;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rrodriguez
 * @param <E>
 * @param <R>
 */
public class HyperDAO<E, R> implements Selectable {

    @PersistenceContext
    protected EntityManager entityManager;

    //Builder
    protected Class<E> entityType;
    public Class<R> resultType;

    public HyperDAO() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();

        this.entityType = (Class<E>) types[0];
        this.resultType = (Class<R>) types[1];
    }

    // Basic DAO methods
    public E saveOrUpdate(E obj) {
        return entityManager.merge(obj);
    }

    public E findById(Object id) {
        return entityManager.find(entityType, id);
    }

    // HyperCriteria API
    @Override
    public Criteria select() {
        return new Criteria(entityManager, entityType, entityType);
    }

    public Criteria selectDTO() {
        return new Criteria(entityManager, entityType, resultType);
    }

    @Override
    public Criteria select(String property) {
        return new Criteria(entityManager, entityType, Projections.property(property));
    }

    //------ Count -----------
    @Override
    public Criteria count() {
        return new Criteria(entityManager, entityType, Projections.count());
    }

    @Override
    public Criteria count(String property) {
        return new Criteria(entityManager, entityType, Projections.count(property));
    }

    @Override
    public Criteria countDistinct() {
        return new Criteria(entityManager, entityType, Projections.countDistinct());
    }

    @Override
    public Criteria countDistinct(String property) {
        return new Criteria(entityManager, entityType, Projections.countDistinct(property));
    }

    @Override
    public Criteria sum(String property, Class<?> resultType) {
        return new Criteria(entityManager, entityType, Projections.sum(property, resultType));
    }

    @Override
    public Criteria min(String property, Class resultType) {
        return new Criteria(entityManager, entityType, Projections.min(property, resultType));
    }

    @Override
    public Criteria max(String property, Class resultType) {
        return new Criteria(entityManager, entityType, Projections.max(property, resultType));
    }

    @Override
    public Criteria avg(String property) {
        return new Criteria(entityManager, entityType, Projections.avg(property));
    }

    @Override
    public Criteria abs(String property, Class resultType) {
        return new Criteria(entityManager, entityType, Projections.abs(property, resultType));
    }

    @Override
    public Criteria abs(SimpleProjection simpleProjection) {
        return new Criteria(entityManager, entityType, Projections.abs(simpleProjection));
    }

    //Visible for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
