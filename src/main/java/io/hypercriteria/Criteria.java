/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria;

import io.hypercriteria.context.QueryContext;
import io.hypercriteria.criterion.Criterion;
import io.hypercriteria.criterion.Order;
import io.hypercriteria.criterion.projection.base.Projection;
import io.hypercriteria.util.AliasInfo;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.JoinType;
import lombok.Getter;
import io.hypercriteria.util.TypeUtil;

/**
 *
 * @author rrodriguez
 */
@Getter
public class Criteria {

    private final EntityManager entityManager;

    // Builder state
    private Class<?> entityType;
    private boolean distinct;
    private String rootAlias;

    public Optional<String> constructorName = Optional.empty();

    // Query structure
    private Optional<Projection> projection = Optional.empty();
    private Projection groupBy;

    private final LinkedHashMap<String, Class> aliasTypeMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, AliasInfo> joinInfoMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, AliasInfo> fetchInfoMap = new LinkedHashMap<>();

    private final List<Criterion> restrictions = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();

    private Optional<Integer> firstResult = Optional.empty();
    private Optional<Integer> maxResults = Optional.empty();

    private Criteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /* =======================
      Builder methods
       ======================= */
    public Criteria from(Class entityType) {
        return from(entityType, "");
    }

    public Criteria from(Class entityType, String rootAlias) {
        this.entityType = entityType;
        this.rootAlias = rootAlias;

        aliasTypeMap.put(rootAlias, entityType);
        return this;
    }

    public Criteria leftJoin(String joinPath, String alias) {
        return join(joinPath, alias, JoinType.LEFT);
    }

    public Criteria innerJoin(String joinPath, String alias) {
        return join(joinPath, alias, JoinType.INNER);
    }

    public Criteria rightJoin(String joinPath, String alias) {
        return join(joinPath, alias, JoinType.RIGHT);
    }

    private Criteria join(String joinPath, String alias, JoinType joinType) {
        Class javaType = TypeUtil.resolveJavaType(joinPath, this);
        aliasTypeMap.put(alias, javaType);
        joinInfoMap.put(joinPath, new AliasInfo(alias, joinType, javaType));
        return this;
    }

    public Criteria fetch(String fetchPath) {
//   TODO
        return this;
    }

    public Criteria usingConstructor(String constructorName) {
        this.constructorName = Optional.of(constructorName);
        return this;
    }

    public Criteria distinct() {
        this.distinct = true;
        return this;
    }

    public Criteria where(Criterion restriction) {
        restrictions.add(restriction);
        return this;
    }

    public Criteria setFirstResult(Integer firstResult) {
        this.firstResult = Optional.of(firstResult);
        return this;
    }

    public Criteria setMaxResults(Integer maxResults) {
        this.maxResults = Optional.of(maxResults);
        return this;
    }

    public Criteria groupBy(Projection groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public Criteria addOrder(Order order) {
        this.orderList.add(order);
        return this;
    }

    /* =======================
       Result execution
       ======================= */
    public Object getSingleResult() {
        return getOptionalResult().orElse(null);
    }

    public Optional<?> getOptionalResult() {
        QueryContext ctx = buildContext();
        Class<?> resultType = resolveResultType(ctx);

        return query(ctx, entityType, resultType)
                .getResultStream()
                .findFirst();
    }

    public <R> R getSingleResult(Class<R> userType) {
        return getOptionalResult(userType).orElse(null);
    }

    public <R> Optional<R> getOptionalResult(Class<R> userType) {
        QueryContext ctx = buildContext();
        Class<?> resolvedType = resolveResultType(ctx);

        validateResultType(userType, resolvedType);

        return query(ctx, entityType, userType)
                .getResultStream()
                .findFirst();
    }

    public List getResultList() {
        QueryContext ctx = buildContext();
        Class resultType = resolveResultType(ctx);

        return query(ctx, entityType, resultType).getResultList();
    }

    public <R> List<R> getResultList(Class<R> userType) {
        QueryContext ctx = buildContext();
        Class<?> resolvedType = resolveResultType(ctx);

        validateResultType(userType, resolvedType);

        return query(ctx, entityType, userType).getResultList();
    }

    /* =======================
       Internals
       ======================= */
    private QueryContext buildContext() {
        return new QueryContext(
                entityManager,
                entityType,
                distinct,
                aliasTypeMap
        );
    }

    private Class<?> resolveResultType(QueryContext ctx) {
        return projection
                .map(p -> p.getReturnType(ctx))
                .orElse(entityType);
    }

    private <R> void validateResultType(Class<R> userType, Class<?> resolvedType) {
        if (!userType.isAssignableFrom(resolvedType)) {
            throw new IllegalArgumentException(
                    "Expected result type " + userType.getName()
                    + " but query resolves to " + resolvedType.getName()
            );
        }
    }

    private <T, R> TypedQuery<R> query(
            QueryContext ctx,
            Class<T> rootType,
            Class<R> resultType
    ) {
        if (rootType == null) {
            throw new IllegalArgumentException(
                    "No root was specified. Please define a from clause."
            );
        }

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<R> criteriaQuery = builder.createQuery(resultType);
        Root<T> root = criteriaQuery.from(rootType);

        if (distinct) {
            criteriaQuery.distinct(true);
        }

        ctx.complete(builder, root, rootAlias, joinInfoMap);

        projection.ifPresent(p -> p.apply(ctx, criteriaQuery));

        if (groupBy != null) {
            criteriaQuery.groupBy(groupBy.toExpression(ctx));
        }

        TypedQuery<R> query = entityManager.createQuery(criteriaQuery);

        firstResult.ifPresent(query::setFirstResult);
        maxResults.filter(m -> m > 0).ifPresent(query::setMaxResults);

        return query;
    }

    // -- Internal Builder --
    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }

    public void setProjection(Projection projection) {
        this.projection = Optional.ofNullable(projection);
    }

    //package visibility
    public static class Builder {

        private final EntityManager entityManager;
        private Class entityType;
        private Projection projection;

        private Builder(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        public static Builder create(EntityManager entityManager) {
            return new Builder(entityManager);
        }

        public Builder entityType(Class entityType) {
            this.entityType = entityType;
            return this;
        }

        public Builder projection(Projection projection) {
            this.projection = projection;
            return this;
        }

        public Criteria build() {
            Criteria criteria = new Criteria(entityManager);
            criteria.setEntityType(entityType);
            criteria.setProjection(projection);
            return criteria;
        }
    }
}
