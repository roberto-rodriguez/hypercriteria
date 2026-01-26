package io.hypercriteria;

import static io.hypercriteria.HyperCriteria.dto;
import static io.hypercriteria.HyperCriteria.property;
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
        return Criteria.Builder.create(entityManager)
                .entityType(entityType)
                .build();
    }

    @Override
    public Criteria select(String property) {
        return select(property(property));
    }

    @Override
    public Criteria select(Class resultType) {
        return select(dto(resultType));
    }

    @Override
    public Criteria select(Projection projection) {
        return Criteria.Builder.create(entityManager)
                .entityType(entityType)
                .projection(projection)
                .build();
    }

    //Visible for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
