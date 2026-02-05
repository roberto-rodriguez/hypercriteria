package io.fluentcriteria;

import static io.fluentcriteria.FluentCriteria.dto;
import io.fluentcriteria.base.Selectable;
import io.fluentcriteria.expression.base.BaseExpression;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static io.fluentcriteria.FluentCriteria.field;

/**
 *
 * @author rrodriguez
 * @param <E>
 */
public class FluentDAO<E> implements Selectable {

    @PersistenceContext
    protected EntityManager entityManager;

    //Builder
    protected Class<E> entityType;

    public FluentDAO() {
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

    // FluentCriteria API
    @Override
    public Criteria select() {
        return Criteria.Builder.create(entityManager)
                .entityType(entityType)
                .build();
    }

    @Override
    public Criteria select(String field) {
        return select(field(field));
    }

    @Override
    public Criteria select(Class resultType) {
        return select(dto(resultType));
    }

    @Override
    public Criteria select(BaseExpression projection) {
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
