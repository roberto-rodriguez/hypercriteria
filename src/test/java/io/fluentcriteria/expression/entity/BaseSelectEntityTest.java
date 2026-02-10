package io.fluentcriteria.expression.entity;

import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.Payment;
import io.sample.model.State;
import io.sample.model.User;
import io.utility.BaseTest;
import static io.utility.CompareUserUtil.assertUserEqualsWithAddress;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Disabled;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectEntityTest extends BaseTest {

    protected UserDAO userDAO;

    private static final User USER_1 = User.builder()
            .firstName("John")
            .lastName("Smith")
            .address(
                    Address.builder()
                            .street("123 Main Street")
                            .city("Atlanta")
                            .zipcode("123456")
                            .state(
                                    State.builder()
                                            .code("GA")
                                            .name("Georgia").build()
                            ).build()
            )
            .build();

    private static final User USER_2 = User.builder()
            .firstName("Jane")
            .lastName("Doe")
            .address(
                    Address.builder()
                            .street("1600 Pensylvania Ave")
                            .city("Whashington")
                            .state(
                                    State.builder()
                                            .code("DC")
                                            .name("District of Columbia").build()
                            ).build()
            )
            .build();

    private static final User USER_WITH_PAYMENTS = USER_1.toBuilder()
            .payments(new ArrayList<>())
            .build();

    static {
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(1D).build());
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(2D).build());
    }

    abstract Object selectEntity();

    abstract Object selectNestedEntity(Class rootType, String path);

    abstract Object selectEntityWithFetchPath(String fetchPath);

    abstract List<User> listEntities();

    abstract List<User> listDistinctEntities();

    abstract List<User> listDistinctEntitiesWithLeftJoinPath(String fetchPath);

    abstract List<User> listDistinctEntitiesWithInnerJoinPath(String fetchPath);

    abstract List<User> listDistinctEntitiesWithLeftFetchPath(String fetchPath);

    abstract List<User> listDistinctEntitiesWithInnerFetchPath(String fetchPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

    @Test
    void testSelectEntity_singleResult() {
        userDAO.saveOrUpdate(USER_1);
        User actual = (User) selectEntity();

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Test
    void testSelectEntity_singleResult_returnNull() {
        User actual = (User) selectEntity();

        Assertions.assertNull(actual);
    }

    @Test
    void testSelectNestedEntity_singleResult() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        User actual = (User) selectNestedEntity(Payment.class, "user");

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Test
    void testSelectEntity_notFetchInternalList() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        User actual = (User) selectEntity();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Assertions.assertFalse(util.isLoaded(actual, "payments"));
    }

    @Test
    void testSelectEntity_fetchingInternalList() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        User actual = (User) selectEntityWithFetchPath("payments");

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Assertions.assertTrue(util.isLoaded(actual, "payments"));

        assertUserEqualsWithAddress(USER_1, actual);

        Assertions.assertEquals(2, actual.getPayments().size());
    }

    @Test
    void testListEntities() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<User> list = listEntities();

        assertEquals(3, list.size());
    }

    @Test
    void testListEntities_returnEmpty() {
        List<User> list = listEntities();

        assertEquals(0, list.size());
    }

    @Test
    void testListDistinctEntities() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntities();//left joins with payments

        assertEquals(1, list.size());
    }

    @Test
    void testListDistinctEntities_leftJoin() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntitiesWithLeftJoinPath("payments");

        assertEquals(2, list.size());
    }

    @Test
    void testSelectEntity_list_innerJoin() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, will be excluded
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntitiesWithInnerJoinPath("payments");

        assertEquals(1, list.size());
    }

    @Test
    void testListDistinctEntities_leftJoinFetch() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        List<User> list = listDistinctEntitiesWithLeftFetchPath("payments");

        assertEquals(2, list.size());
    }

    //This ensures explicit joins declared but not referenced are still applied
    @Test
    void testSelectEntity_list_innerJoinFetch() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, will be excluded
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        List<User> list = listDistinctEntitiesWithInnerFetchPath("payments");

        assertEquals(1, list.size());
    }

    // --- Edge cases ---
    abstract User selectUserWithMultipleFetches();

    abstract User duplicatedAlias_throwsException();

    abstract User noFromClause_throwsException();

    abstract Long fetchWithProjection_throwsException();

    @Test
    void testSelectEntity_multipleFetches() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear();

        User actual = selectUserWithMultipleFetches();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertTrue(util.isLoaded(actual, "payments"));
        assertTrue(util.isLoaded(actual, "address"));
    }

    @Test
    void testDuplicateAlias_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> duplicatedAlias_throwsException());
    }

    @Test
    void testNoFromClause_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> noFromClause_throwsException());
    }

    @Test
    void testFetchWithProjection_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> fetchWithProjection_throwsException());
    }

    //--- Cover fetch with all variants ------------------------------------- 
    //Fetching User.address
    abstract User fetchOneToOneAddress();

    @Test
    void testFetchOneToOneAddress() {
        userDAO.saveOrUpdate(USER_1);

        entityManager.flush();
        entityManager.clear();

        User actual = fetchOneToOneAddress();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertTrue(util.isLoaded(actual, "address"));

        // state should NOT be loaded because we didn't fetch it
        assertFalse(util.isLoaded(actual.getAddress(), "state"));
    }

    //Fetch User → Address → State
    abstract User fetchNestedManyToOne();

    @Test
    void testFetchNestedManyToOne() {
        userDAO.saveOrUpdate(USER_1);

        entityManager.flush();
        entityManager.clear();

        User actual = fetchNestedManyToOne();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertTrue(util.isLoaded(actual, "address"));
        assertTrue(util.isLoaded(actual.getAddress(), "state"));
    }

    //Fetch Payment → User → Address  
    abstract Payment fetchCollectionThenToOne();

    @Test
    void testFetchCollectionThenToOne() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear();

        // Start from Payment as root
        Payment payment = fetchCollectionThenToOne();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertTrue(util.isLoaded(payment, "user"));
        assertTrue(util.isLoaded(payment.getUser(), "address"));
    }

    //Fetch  "payments" and "address.state"
    // User → Payment 
    // User → Address → State
    abstract User fetchMultipleRelationsMixed();

    @Test
    void testFetchMultipleRelationsMixed() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear();

        User actual = fetchMultipleRelationsMixed();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertTrue(util.isLoaded(actual, "payments"));
        assertTrue(util.isLoaded(actual, "address"));
        assertTrue(util.isLoaded(actual.getAddress(), "state"));
    }

    abstract User duplicateFetchPathThrowsException();

    @Test
    void testDuplicateFetchPathThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> duplicateFetchPathThrowsException());
    }

    abstract User joinAndFetchSamePath();

    @Test
    void testJoinAndFetchSamePath() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear();

        User actual = joinAndFetchSamePath();

        assertTrue(
                entityManager.getEntityManagerFactory()
                        .getPersistenceUnitUtil()
                        .isLoaded(actual, "payments")
        );
    }

    abstract User innerFetchToOne();

    @Test
    void testInnerFetchToOne() {
        userDAO.saveOrUpdate(USER_1);

        entityManager.flush();
        entityManager.clear();

        User actual = innerFetchToOne();

        assertNotNull(actual.getAddress());
    }

}
