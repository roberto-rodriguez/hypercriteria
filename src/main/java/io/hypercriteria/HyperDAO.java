package io.hypercriteria;

import io.hypercriteria.base.Selectable;
import io.hypercriteria.criterion.projection.base.Projection;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rrodriguez
 * @param <E>
 */
public class HyperDAO<E> implements Selectable {

    @PersistenceContext
    protected EntityManager entityManager;

    //Builder
    protected Class<E> entityType;

    public HyperDAO() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();

        this.entityType = (Class<E>) types[0];
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

    @Override
    public Criteria select(String property) {
        return new Criteria(entityManager, entityType, HyperCriteria.property(property));
    }

    @Override
    public Criteria select(Class resultType) {
        return new Criteria(entityManager, entityType, resultType);
    }

    @Override
    public Criteria select(Projection projection) {
        return new Criteria(entityManager, entityType, projection);
    }

    //Visible for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
