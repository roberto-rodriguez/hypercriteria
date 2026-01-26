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

    //Builder
    private Class<?> entityType;
    private Class<?> resultType;
    private boolean distinct;

    private String rootAlias;
    public Optional<String> constructorName = Optional.empty();

    //Intermediate
    private Optional<Projection> projection = Optional.empty();
    private Projection groupBy;//TODO: Make a projection for DTOs too

    private final LinkedHashMap<String, Class> aliasTypeMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, AliasInfo> joinInfoMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, AliasInfo> fetchInfoMap = new LinkedHashMap<>();

    private final List<Criterion> restrictions = new ArrayList<>();

    private final List<Order> orderList = new ArrayList();

    private Optional<Integer> firstResult = Optional.empty();
    private Optional<Integer> maxResults = Optional.empty();

    //package visibility
    private Criteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Criteria from(Class entityType) {
        return from(entityType, "");
    }

    public Criteria from(Class entityType, String rootAlias) {
        this.entityType = entityType;
        this.rootAlias = rootAlias;

        if (this.resultType == null) {
            this.resultType = entityType;
        }

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
//        if (this.resultType == null) {
//            throw new IllegalArgumentException("Fetch clause is expected to be after 'from'. ");
//        }
//        PathUtil.getPathInfo(entityManager, resultType, fetchPath, fetchToAliasJoinTypeMap);
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

    public <R> Object getSingleResult(Class<R> resultType) {
        this.resultType = resultType;
        return (R) getSingleResult();
    }

    public Object getSingleResult() {
        return query()
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List getResultList() {
        return (List) query().getResultList();
    }

    private TypedQuery query() {
        if (entityType == null) {
            throw new IllegalArgumentException("No root was specified. Please define a from clause, specifying the root entity.");
        }

        // Initialize context
        QueryContext ctx = new QueryContext(
                entityManager,
                entityType,
                distinct,
                aliasTypeMap
        );

        // Get return type
        if (projection.isPresent()) {
            this.resultType = projection.get().getReturnType(ctx);
        }

        // Create builder and root
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery(resultType);

        Root root = criteriaQuery.from(entityType);

        if (distinct && entityType == resultType) {
            criteriaQuery.distinct(true);
        }

        // Complete context once we have the root
        ctx.complete(builder, root, rootAlias, joinInfoMap);

        if (projection.isPresent()) {
            projection.get().apply(ctx, criteriaQuery);
        }

        if (groupBy != null) {
            criteriaQuery.groupBy(groupBy.toExpression(ctx));
        }

//        if (!orderList.isEmpty()) {
//            criteriaQuery.orderBy(orderList.stream()
//                    .map(o -> o.build(builder, joinMap))
//                    .collect(Collectors.toList()));
//        }
        TypedQuery query = entityManager.createQuery(criteriaQuery);

        if (firstResult.isPresent()) {
            query.setFirstResult(firstResult.get());
        }

        if (maxResults.isPresent() && maxResults.get() > 0) {
            query.setMaxResults(maxResults.get());
        }
        return query;
    }

    // -- Builder --
    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }

    public void setProjection(Projection projection) {
        this.projection = Optional.ofNullable(projection);
    }

    //package visibility
    public static class Builder {

        private final EntityManager entityManager;
        private Class entityType;
        private Class resultType;
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

        public Builder resultType(Class resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder projection(Projection projection) {
            this.projection = projection;
            return this;
        }

        public Criteria build() {
            if (resultType == null) {
                resultType = entityType;//By default set entityType as resultType
            }

            Criteria criteria = new Criteria(entityManager);
            criteria.setEntityType(entityType);
            criteria.setResultType(resultType);
            criteria.setProjection(projection);
            return criteria;
        }
    }
}
