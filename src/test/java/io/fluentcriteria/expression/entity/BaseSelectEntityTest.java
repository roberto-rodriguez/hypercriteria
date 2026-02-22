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

    private static final User USER_WITH_ONE_PAYMENT = USER_1.toBuilder()
            .payments(new ArrayList<>())
            .build();

    private static final User USER_WITH_PAYMENTS = USER_1.toBuilder()
            .payments(new ArrayList<>())
            .build();

    //Entities to test on clause
    private static final User USER_NO_PAYMENTS = User.builder()
            .firstName("NoPay")
            .lastName("User")
            .payments(new ArrayList<>())
            .build();

    private static final User USER_PAY_1_ONLY = User.builder()
            .firstName("OnePay")
            .lastName("User")
            .payments(new ArrayList<>())
            .build();

    private static final User USER_PAY_1_AND_2 = User.builder()
            .firstName("TwoPay")
            .lastName("User")
            .payments(new ArrayList<>())
            .build();

    private static final User USER_PAY_0_5_ONLY = User.builder()
            .firstName("SmallPay")
            .lastName("User")
            .payments(new ArrayList<>())
            .build();

    static {
        USER_WITH_ONE_PAYMENT.addPayment(Payment.builder().amount(1D).build());
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(1D).build());
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(2D).build());

        USER_PAY_1_ONLY.addPayment(Payment.builder().amount(1D).build());

        USER_PAY_1_AND_2.addPayment(Payment.builder().amount(1D).build());
        USER_PAY_1_AND_2.addPayment(Payment.builder().amount(2D).build());

        USER_PAY_0_5_ONLY.addPayment(Payment.builder().amount(0.5D).build());
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

    @Disabled
    void testSelectEntity_singleResult() {
        userDAO.saveOrUpdate(USER_1);
        User actual = (User) selectEntity();

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Disabled
    void testSelectEntity_singleResult_returnNull() {
        User actual = (User) selectEntity();

        Assertions.assertNull(actual);
    }

    @Disabled
    void testSelectNestedEntity_singleResult() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        User actual = (User) selectNestedEntity(Payment.class, "user");

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Disabled
    void testSelectEntity_notFetchInternalList() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        User actual = (User) selectEntity();

        PersistenceUnitUtil util
                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        Assertions.assertFalse(util.isLoaded(actual, "payments"));
    }

    @Disabled
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

    @Disabled
    void testListEntities() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<User> list = listEntities();

        assertEquals(3, list.size());
    }

    @Disabled
    void testListEntities_returnEmpty() {
        List<User> list = listEntities();

        assertEquals(0, list.size());
    }

    @Disabled
    void testListDistinctEntities() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntities();//left joins with payments

        assertEquals(1, list.size());
    }

    @Disabled
    void testListDistinctEntities_leftJoin() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntitiesWithLeftJoinPath("payments");

        assertEquals(2, list.size());
    }

    @Disabled
    void testSelectEntity_list_innerJoin() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, will be excluded
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntitiesWithInnerJoinPath("payments");

        assertEquals(1, list.size());
    }

    @Disabled
    void testListDistinctEntities_leftJoinFetch() {
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        List<User> list = listDistinctEntitiesWithLeftFetchPath("payments");

        assertEquals(2, list.size());
    }

    //This ensures explicit joins declared but not referenced are still applied
    @Disabled
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

    @Disabled
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

    @Disabled
    void testDuplicateAlias_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> duplicatedAlias_throwsException());
    }

    @Disabled
    void testNoFromClause_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> noFromClause_throwsException());
    }

    @Disabled
    void testFetchWithProjection_throwsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> fetchWithProjection_throwsException());
    }

    //--- Cover fetch with all variants ------------------------------------- 
    //Fetching User.address
    abstract User fetchOneToOneAddress();

    @Disabled
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

    @Disabled
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

    @Disabled
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

    @Disabled
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

    @Disabled
    void testDuplicateFetchPathThrowsException() {
        assertThrows(IllegalArgumentException.class, ()
                -> duplicateFetchPathThrowsException());
    }

    abstract User joinAndFetchSamePath();

    @Disabled
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

    @Disabled
    void testInnerFetchToOne() {
        userDAO.saveOrUpdate(USER_1);

        entityManager.flush();
        entityManager.clear();

        User actual = innerFetchToOne();

        assertNotNull(actual.getAddress());
    }

    abstract List<User> listUsers_twoExplicitJoinsSamePathDifferentAlias();

    @Disabled
    void testTwoExplicitJoinsSamePathDifferentAlias() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear();

        List<User> list = listUsers_twoExplicitJoinsSamePathDifferentAlias();
        //One user joins with two Payment twice, produce 4 records 
        assertEquals(4, list.size());
    }

    abstract List<User> listUsers_innerJoinDeclaredButNeverReferenced_excludesRows();

    @Disabled
    void testInnerJoinDeclaredButNeverReferenced_excludesRows() {
        userDAO.saveOrUpdate(USER_1);            // no payments
        userDAO.saveOrUpdate(USER_WITH_ONE_PAYMENT);

        entityManager.flush();
        entityManager.clear();

        List<User> list = listUsers_innerJoinDeclaredButNeverReferenced_excludesRows();

        // Because inner join to payments should exclude USER_1 even if payments never referenced
        assertEquals(1, list.size());
    }

    abstract List<User> listUsers_explicitJoinAndImplicitJoinSamePath();

    @Disabled
    void testExplicitJoinAndImplicitJoinSamePath() {
        userDAO.saveOrUpdate(USER_1);

        entityManager.flush();
        entityManager.clear();

        List<User> list = listUsers_explicitJoinAndImplicitJoinSamePath();

        assertEquals(1, list.size());
    }

    //----------------------------------------------------------------------
    //-------------------------- Testing 'ON' clause ----------------
    // ---------------------------------------------------------------------
    abstract void on_withoutJoin_throws();

    abstract List<User> listUsers_innerJoinPayments_onAmountGreaterThan(double amount);

    abstract List<User> listUsers_leftJoinWithOn_thenImplicitWhereOnPaymentsAmountGreaterThan(double amount);

    abstract List<User> listUsers_leftJoinP1WithOn_thenLeftJoinP2WithoutOn_whereP2AmountGreaterThan(double amount);

    abstract List<User> listUsers_onCalledTwice_lastOneWins(double firstThreshold, double secondThreshold);

    /* =======================
       Tests
       ======================= */
    @Disabled
    void testOn_withoutJoin_throws() {
        on_withoutJoin_throws();
    }

    @Disabled
    void testInnerJoin_onClause_filtersRoots() {
        userDAO.saveOrUpdate(USER_NO_PAYMENTS);
        userDAO.saveOrUpdate(USER_PAY_1_ONLY);
        userDAO.saveOrUpdate(USER_PAY_1_AND_2);

        // INNER JOIN payments p ON p.amount > 1.5  => only USER_PAY_1_AND_2 matches
        List<User> users = listUsers_innerJoinPayments_onAmountGreaterThan(1.5D);

        assertEquals(1, users.size());
        assertEquals("TwoPay", users.get(0).getFirstName());
    }

    @Disabled
    void testExplicitJoinWithOn_isNotReusedByImplicitJoin() {
        userDAO.saveOrUpdate(USER_PAY_0_5_ONLY);

        /*
          Query shape we want to test:

          from User u
          left join u.payments p on p.amount > 1
          where (implicit join) payments.amount > 0

          Expected: user SHOULD be returned because implicit join must NOT reuse the explicit
          join-with-on. If it incorrectly reuses it, there is no joined row (0.5 is not > 1),
          and the where could incorrectly filter out the user.
         */
        List<User> users = listUsers_leftJoinWithOn_thenImplicitWhereOnPaymentsAmountGreaterThan(0D);

        assertEquals(1, users.size());
        assertEquals("SmallPay", users.get(0).getFirstName());
    }

    @Disabled
    void testJoinWithOn_isNotReusedByAnotherExplicitJoinAlias() {
        userDAO.saveOrUpdate(USER_PAY_0_5_ONLY);

        /*
          from User u
          left join u.payments p1 on p1.amount > 1
          left join u.payments p2
          where p2.amount > 0

          Expected: user returned (via p2=0.5). If p2 reuses p1 join spec,
          user would be filtered out.
         */
        List<User> users = listUsers_leftJoinP1WithOn_thenLeftJoinP2WithoutOn_whereP2AmountGreaterThan(0D);

        assertEquals(1, users.size());
        assertEquals("SmallPay", users.get(0).getFirstName());
    }

    @Test
    void testOnCalledTwice_lastOneWins() {
        userDAO.saveOrUpdate(USER_PAY_1_AND_2);

        // First ON: > 0.5 (would allow both 1 and 2)
        // Second ON: > 1.5 (should restrict to only payment=2)
        // We assert behavior indirectly by requiring threshold 1.5 to match.
        List<User> users = listUsers_onCalledTwice_lastOneWins(0.5D, 1.5D);

        assertEquals(1, users.size());
        assertEquals("TwoPay", users.get(0).getFirstName());
    }

}
