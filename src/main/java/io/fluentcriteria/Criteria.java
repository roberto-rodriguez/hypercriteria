/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.criterion.Order;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.BasePredicate;
import io.fluentcriteria.predicate.builder.CriteriaPredicateBuilder;
import io.fluentcriteria.predicate.builder.WhereBuilder;
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
import io.fluentcriteria.util.TypeUtil;
import static io.fluentcriteria.FluentCriteria.field;
import io.fluentcriteria.context.JoinSpec;
import io.fluentcriteria.predicate.And;
import io.fluentcriteria.predicate.Or;
import io.fluentcriteria.predicate.builder.AndBuilder;
import io.fluentcriteria.predicate.builder.OrBuilder;

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
    private Optional<BaseExpression> projection = Optional.empty();
    private BaseExpression groupBy;

    private final LinkedHashMap<String, Class> aliasTypeMap = new LinkedHashMap<>();
    private final List<JoinSpec> explicitJoinSpecs = new ArrayList<>();

    private Optional<BasePredicate> predicate = Optional.empty();
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

        // Register root alias type map for "u.*" paths
        setAliasType(rootAlias, entityType);

        // Register root as an explicit "join spec" with blank path
        explicitJoinSpecs.add(new JoinSpec("", rootAlias, JoinType.LEFT, false, entityType));
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
        return join(joinPath, alias, joinType, javaType);
    }

    private Criteria join(String joinPath, String alias, JoinType joinType, Class javaType) {
        setAliasType(alias, javaType);
        explicitJoinSpecs.add(new JoinSpec(joinPath, alias, joinType, false, javaType));
        return this;
    }

    public Criteria leftJoinFetch(String fetchPath) {
        return fetch(fetchPath, null, JoinType.LEFT);
    }

    public Criteria leftJoinFetch(String fetchPath, String alias) {
        return fetch(fetchPath, alias, JoinType.LEFT);
    }

    public Criteria innerJoinFetch(String fetchPath) {
        return fetch(fetchPath, null, JoinType.INNER);
    }

    public Criteria innerJoinFetch(String fetchPath, String alias) {
        return fetch(fetchPath, alias, JoinType.INNER);
    }

    private Criteria fetch(String joinPath, String alias, JoinType joinType) {
        Class javaType = TypeUtil.resolveJavaType(joinPath, this);
        setAliasType(alias, javaType);
        explicitJoinSpecs.add(new JoinSpec(joinPath, alias, joinType, true, javaType));
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

    public Criteria where(BasePredicate predicate) {
        return and(predicate);
    }

    public CriteriaPredicateBuilder where(BaseExpression expression) {
        return new WhereBuilder(this, expression);
    }

    public CriteriaPredicateBuilder where(String fieldPath) {
        return new WhereBuilder(this, field(fieldPath));
    }

    public Criteria and(BasePredicate predicate) {
        if (this.predicate.isEmpty()) {
            this.predicate = Optional.of(predicate);
        } else {
            if (this.predicate.get() instanceof And andPredicate) {
                andPredicate.add(predicate);
            }
            if (this.predicate.get() instanceof Or) {
                throw new IllegalArgumentException("Ambiguos combination of OR and AND predicates. Consider using nested and/or instead.");
            }
            this.predicate = Optional.of(FluentCriteria.and(this.predicate.get(), predicate));
        }
        return this;
    }

    public CriteriaPredicateBuilder and(BaseExpression expression) {
        return new AndBuilder(this, expression);
    }

    public CriteriaPredicateBuilder and(String fieldPath) {
        return new AndBuilder(this, field(fieldPath));
    }

    public Criteria or(BasePredicate predicate) {
        if (this.predicate.isEmpty()) {
            this.predicate = Optional.of(predicate);
        } else {
            if (this.predicate.get() instanceof And) {
                throw new IllegalArgumentException("Ambiguos combination of OR and AND predicates. Consider using nested and/or instead.");

            }
            if (this.predicate.get() instanceof Or orPredicate) {
                orPredicate.add(predicate);
            }
            this.predicate = Optional.of(FluentCriteria.or(this.predicate.get(), predicate));
        }
        return this;
    }

    public CriteriaPredicateBuilder or(BaseExpression expression) {
        return new OrBuilder(this, expression);
    }

    public CriteriaPredicateBuilder or(String fieldPath) {
        return new OrBuilder(this, field(fieldPath));
    }

    public Criteria setFirstResult(Integer firstResult) {
        this.firstResult = Optional.of(firstResult);
        return this;
    }

    public Criteria setMaxResults(Integer maxResults) {
        this.maxResults = Optional.of(maxResults);
        return this;
    }

    public Criteria groupBy(BaseExpression groupBy) {
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
                aliasTypeMap,
                explicitJoinSpecs
        );
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

        ctx.complete(builder, root, rootAlias);

        //----- Fetch ------- 
        boolean hasFetch = explicitJoinSpecs.stream().anyMatch(JoinSpec::isFetch);
        if (projection.isPresent() && hasFetch) {
            throw new IllegalArgumentException("Fetch joins are only allowed when selecting the root entity");
        }

        // --------------
        projection.ifPresent(p -> p.apply(ctx, criteriaQuery));

        if (distinct || hasFetch) {
            criteriaQuery.distinct(true);
        }

        if (predicate.isPresent()) {
            criteriaQuery.where(predicate.get().toPredicate(ctx));
        }

        if (groupBy != null) {
            criteriaQuery.groupBy(groupBy.toExpression(ctx));
        }

        ctx.initializeUnusedExplicitJoins();

        TypedQuery<R> query = entityManager.createQuery(criteriaQuery);

        firstResult.ifPresent(query::setFirstResult);
        maxResults.filter(m -> m > 0).ifPresent(query::setMaxResults);

        return query;
    }

    // ------- Utility and validation methods
    private Class<?> resolveResultType(QueryContext ctx) {
        return projection
                .map(p -> p.getReturnType(ctx))
                .orElse(entityType);
    }

    private <R> void validateResultType(Class<R> userType, Class<?> resolvedType) {
        if (resolvedType == null) {
            return; //Exception will be thrown latter on
        }
        if (!userType.isAssignableFrom(resolvedType)) {
            throw new IllegalArgumentException(
                    "Expected result type " + userType.getName()
                    + " but query resolves to " + resolvedType.getName()
            );
        }
    }

    private void setAliasType(String alias, Class<?> type) {
        // Alias collision check
        if (alias == null || alias.isEmpty()) {
            return;
        }

        if (aliasTypeMap.containsKey(alias)) {
            throw new IllegalArgumentException("Alias '" + alias + "' is duplicated.");
        }

        aliasTypeMap.put(alias, type);
    }

    // -- Internal Builder --
    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }

    public void setProjection(BaseExpression projection) {
        this.projection = Optional.ofNullable(projection);
    }

    //package visibility
    public static class Builder {

        private final EntityManager entityManager;
        private Class entityType;
        private BaseExpression projection;

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

        public Builder projection(BaseExpression projection) {
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
