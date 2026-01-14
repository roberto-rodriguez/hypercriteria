/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.hypercriteria;

import io.hypercriteria.criterion.Criterion;
import io.hypercriteria.criterion.Order;
import io.hypercriteria.criterion.predicate.base.Alias;
import io.hypercriteria.criterion.projection.base.Projection;
import io.hypercriteria.util.ProjectionBuilder;
import io.hypercriteria.util.AliasBuilder;
import io.hypercriteria.util.AliasJoinType;
import io.hypercriteria.util.PathUtil;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.FetchParent;
import lombok.Getter;

/**
 *
 * @author rrodriguez
 */
@Getter
public class Criteria<E, R> {

    private final EntityManager entityManager;

    //Builder
    protected Class<E> entityType;
    public Class<R> resultType;
    public boolean distinct;

    public Optional<String> constructorName = Optional.empty();

    //Intermediate
    private Optional<Projection> userSpecifiedProjection = Optional.empty();

    private final LinkedHashMap<String, AliasJoinType> joinToAliasJoinTypeMap = new LinkedHashMap<>();
    private final LinkedHashMap<String, AliasJoinType> fetchToAliasJoinTypeMap = new LinkedHashMap<>();

    //User specified alias. Allows uses to specify different JoinType for specific alias.
//    private List<Alias> aliasList = new ArrayList<>();
    private final List<Criterion> restrictions = new ArrayList<>();

    private final List<Order> orderList = new ArrayList();

    private Optional<Integer> firstResult = Optional.empty();
    private Optional<Integer> maxResults = Optional.empty();

    public Criteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Criteria(EntityManager entityManager, Class<E> entityType, Class<R> resultType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
        this.resultType = resultType;
    }

    public Criteria(EntityManager entityManager, Projection projection) {
        this.entityManager = entityManager;
        this.userSpecifiedProjection = Optional.of(projection);
    }

    public Criteria(EntityManager entityManager, Class<E> entityType, Projection projection) {
        this.entityType = entityType;
        this.entityManager = entityManager;
        this.userSpecifiedProjection = Optional.of(projection);
    }

    public Criteria(EntityManager entityManager, Class<R> resultType) {
        this.entityManager = entityManager;
        this.resultType = resultType;
    }

    public Criteria from(Class entityType) {
        this.entityType = entityType;

        if (this.resultType == null) {
            this.resultType = entityType;
        }

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

    /**
     * Same as fetch from JPA Criteria, but specifying nested fetch as a
     * join-aware field path.
     *
     *
     * <p>
     * @param fetchPath: The field path supports the following join delimiters:
     * </p>
     *
     * <ul>
     * <li><b>.</b> → LEFT join (default)</li>
     * <li><b>&gt;</b> → RIGHT join</li>
     * <li><b>&lt;&gt;</b> → INNER join</li>
     * </ul>
     * Example: For a having a List of Orders, which have a List of Items: - To
     * read users fetching Orders: .fetch("orders") - To read only users having
     * Orders (LEFT JOIN): .fetch("<>orders") - To fetch Orders and Items:
     * .fetch("orders.items") - To fetch only Orders having Items (User LEFT
     * JOIN Order INNER JOIN Items): .fetch("orders<>items")
     * @return
     *
     *
     */
    public Criteria fetch(String fetchPath) {
        if (this.resultType == null) {
            throw new IllegalArgumentException("Please specify which entity are you querying from. Make sure the fetch clause is specified after 'from'. ");
        }
        PathUtil.getPathInfo(entityManager, resultType, fetchPath, fetchToAliasJoinTypeMap);
        return this;
    }

    public Criteria addOrder(Order order) {
        this.orderList.add(order);
        return this;
    }

    public R getSingleResult(Class<R> resultType) {
        this.resultType = resultType;
        return getSingleResult();
    }

    public R getSingleResult() {
        return query(entityType, resultType)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<R> getResultList() {
        return (List<R>) query(entityType, resultType).getResultList();
    }

    private <E, R> TypedQuery<R> query(Class<E> entityType, Class<R> resultType) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        System.out.println("CriteriaBuilder builder = entityManager.getCriteriaBuilder();");

        Optional<Projection> projection = ProjectionBuilder.build(this);

        if (projection.isPresent()) {
            resultType = projection.get().getReturnType().orElse(resultType);
        }

        CriteriaQuery<R> criteriaQuery = builder.createQuery(resultType);
        System.out.println("CriteriaQuery<R> criteriaQuery = builder.createQuery(" + resultType + ");");

        if (entityType == null) {
            throw new IllegalArgumentException("No criteria query roots were specified.");
        }

        Root<E> root = criteriaQuery.from(entityType);

        if (distinct && entityType == resultType) {
            criteriaQuery.distinct(true);
        }

        //Joins
        final Map<String, From> joinMap = new HashMap<>();
        joinMap.put("", root);

        if (!joinToAliasJoinTypeMap.isEmpty()) {
            List<Alias> aliasList = AliasBuilder.build(joinToAliasJoinTypeMap);

            for (Alias alias : aliasList) {
                alias.join(joinMap);
            }
        }

        //Fetch
        if (!fetchToAliasJoinTypeMap.isEmpty()) {
            final Map<String, FetchParent> fetchMap = new HashMap<>();
            fetchMap.put("", root);

            List<Alias> fetchList = AliasBuilder.build(fetchToAliasJoinTypeMap);

            for (Alias alias : fetchList) {
                alias.fetch(fetchMap);
            }
        }

        Predicate[] predicates = restrictions.stream()
                .map(r -> r.getPredicate(builder, joinMap))
                .toArray(Predicate[]::new);

        if (predicates.length > 0) {
            criteriaQuery.where(builder.and(predicates));
        }

        if (projection.isPresent()) {
            projection.get().apply(this, builder, criteriaQuery, joinMap);
            projection.get().applyGroupBy(builder, criteriaQuery, joinMap);
        }

        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(orderList.stream()
                    .map(o -> o.build(builder, joinMap))
                    .collect(Collectors.toList()));
        }

        TypedQuery<R> query = entityManager.createQuery(criteriaQuery);

        if (firstResult.isPresent()) {
            query.setFirstResult(firstResult.get());
        }

        if (maxResults.isPresent() && maxResults.get() > 0) {
            query.setMaxResults(maxResults.get());
        }
        return query;
    }
}
