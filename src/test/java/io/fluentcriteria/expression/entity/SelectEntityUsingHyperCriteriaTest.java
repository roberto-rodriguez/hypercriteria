package io.fluentcriteria.expression.entity;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.Predicates.greaterThan;
import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingHyperCriteriaTest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getSingleResult();
    }

    @Override
    Object selectNestedEntity(Class rootType, String path) {
        return FluentCriteria.using(entityManager)
                .select(path)
                .from(rootType)
                .getSingleResult();
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntities() {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .leftJoin("payments", "p")
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftJoinPath(String joinPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .leftJoin(joinPath, "a")
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerJoinPath(String joinPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .innerJoin(joinPath, "a")
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .leftJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerFetchPath(String fetchPath) {
        return FluentCriteria.using(entityManager)
                .select()
                .distinct()
                .from(User.class)
                .innerJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    User selectUserWithMultipleFetches() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch("payments")
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }

    @Override
    User duplicatedAlias_throwsException() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoin("payments", "a")
                .leftJoin("address", "a")
                .getSingleResult(User.class);
    }

    @Override
    User noFromClause_throwsException() {
        return FluentCriteria.using(entityManager)
                .select()
                .getSingleResult(User.class);
    }

    @Override
    Long fetchWithProjection_throwsException() {
        return FluentCriteria.using(entityManager)
                .select("id")
                .from(User.class)
                .leftJoinFetch("payments", "a")
                .getSingleResult(Long.class);
    }

    @Override
    User fetchOneToOneAddress() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }

    @Override
    User fetchNestedManyToOne() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .where("address.state.id").greaterThan(0)//reuse same join
                .leftJoinFetch("address.state")
                .getSingleResult(User.class);
    }

    @Override
    Payment fetchCollectionThenToOne() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(Payment.class)
                .where("user.address.id").greaterThan(0)//reuse same join
                .leftJoinFetch("user.address")
                .getSingleResult(Payment.class);
    }

    @Override
    User fetchMultipleRelationsMixed() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .where("address.state.id").greaterThan(0)//reuse same join explicitly
                .and(greaterThan("s.id", -1))//reuse same join with alias
                .leftJoinFetch("payments")
                .leftJoinFetch("address", "a") //This also worked: .leftJoinFetch("address.state")
                .leftJoinFetch("a.state", "s")
                .getSingleResult(User.class);
    }

    @Override
    User duplicateFetchPathIsIgnored() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch("payments")
                .leftJoinFetch("payments")
                .getSingleResult(User.class);
    }

    @Override
    User joinAndFetchSamePath() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoin("payments", "p")
                .leftJoinFetch("payments")
                .where("p.id").greaterThan(0) //Make sure still produces only one join
                .getSingleResult(User.class);
    }

    @Override
    User innerFetchToOne() {
        return FluentCriteria.using(entityManager)
                .select()
                .from(User.class)
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }
}
