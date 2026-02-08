package io.fluentcriteria.expression.entity;

import io.sample.dao.PaymentDAO;
import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectEntityUsingDAOTest extends BaseSelectEntityTest {

    @Override
    Object selectEntity() {
        return userDAO.select().getSingleResult();
    }

    @Override
    Object selectNestedEntity(Class rootType, String path) {
        return userDAO.select(path)
                .from(rootType)
                .getSingleResult();
    }

    @Override
    Object selectEntityWithFetchPath(String fetchPath) {
        return userDAO
                .select()
                .leftJoinFetch(fetchPath)
                .getSingleResult();
    }

    @Override
    List<User> listEntities() {
        return userDAO.select().getResultList();
    }

    @Override
    List<User> listDistinctEntities() {
        return userDAO
                .select()
                .distinct()
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftJoinPath(String joinPath) {
        return userDAO.select()
                .distinct()
                .leftJoin(joinPath, "a")
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerJoinPath(String joinPath) {
        return userDAO.select()
                .distinct()
                .innerJoin(joinPath, "a")
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithLeftFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .leftJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    List<User> listDistinctEntitiesWithInnerFetchPath(String fetchPath) {
        return userDAO.select()
                .distinct()
                .innerJoinFetch(fetchPath)
                .getResultList();
    }

    @Override
    User selectUserWithMultipleFetches() {
        return userDAO
                .select()
                .leftJoinFetch("payments")
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }

    @Override
    User duplicatedAlias_throwsException() {
        return userDAO
                .select()
                .leftJoin("payments", "a")
                .leftJoin("address", "a")
                .getSingleResult(User.class);
    }

    @Override
    User noFromClause_throwsException() {
        throw new IllegalArgumentException();
    }

    @Override
    Long fetchWithProjection_throwsException() {
        return userDAO
                .select("id")
                .from(User.class)
                .leftJoinFetch("payments", "a")
                .getSingleResult(Long.class);
    }

    @Override
    User fetchOneToOneAddress() {
        return userDAO
                .select()
                .from(User.class)
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }

    @Override
    User fetchNestedManyToOne() {
        return userDAO
                .select()
                .leftJoinFetch("address.state")
                .getSingleResult(User.class);
    }

    @Override
    Payment fetchCollectionThenToOne() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually 

        return paymentDAO
                .select()
                .where("user.address.id").greaterThan(0)//reuse same join
                .leftJoinFetch("user.address")
                .getSingleResult(Payment.class);
    }

    @Override
    User fetchMultipleRelationsMixed() {
        return userDAO
                .select()
                .from(User.class)
                .where("address.state.id").greaterThan(0)//reuse same join
                .leftJoinFetch("payments")
                .leftJoinFetch("address.state")
                .getSingleResult(User.class);
    }

    @Override
    User duplicateFetchPathIsIgnored() {
        return userDAO
                .select()
                .from(User.class)
                .leftJoinFetch("payments")
                .leftJoinFetch("payments")
                .getSingleResult(User.class);
    }

    @Override
    User joinAndFetchSamePath() {
        return userDAO
                .select()
                .from(User.class)
                .leftJoin("payments", "p")
                .leftJoinFetch("payments")
                .getSingleResult(User.class);
    }
    
     @Override
    User innerFetchToOne() {
        return userDAO
                .select()
                .from(User.class)
                .leftJoinFetch("address")
                .getSingleResult(User.class);
    }
}
