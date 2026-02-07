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
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectEntityTest extends BaseTest {

    private static boolean DISABLE_ALL = false;

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

    List<User> sample() {
        return null;
    }

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

    @Test
    void testSelectEntity_singleResult() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);
        User actual = (User) selectEntity();

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Test
    void testSelectNestedEntity_singleResult() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        User actual = (User) selectNestedEntity(Payment.class, "user");

        assertUserEqualsWithAddress(USER_1, actual);
    }

    @Test
    void testSelectEntity_notFetchInternalList() {
        if (DISABLE_ALL) {
            return;
        }
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
        if (DISABLE_ALL) {
            return;
        }
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
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<User> list = listEntities();

        assertEquals(3, list.size());
    }

    @Test
    void testListDistinctEntities() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        List<User> list = listDistinctEntities();//left joins with payments

        assertEquals(1, list.size());
    }

    @Test
    void testListDistinctEntities_leftJoin() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
 
        List<User> list = listDistinctEntitiesWithLeftJoinPath("payments");

        assertEquals(2, list.size());
    }

    @Test
    void testSelectEntity_list_innerJoin() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);  //No Payments, will be excluded
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
 
        List<User> list = listDistinctEntitiesWithInnerJoinPath("payments");

        assertEquals(1, list.size());
    }

    @Test
    void testListDistinctEntities_leftJoinFetch() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);  //No Payments, still will be included
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        List<User> list = listDistinctEntitiesWithLeftFetchPath("payments");

        assertEquals(2, list.size());
    }

    @Test
    void testSelectEntity_list_innerJoinFetch() {
         if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);  //No Payments, will be excluded
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        entityManager.flush();
        entityManager.clear(); // detach all entities

        List<User> list = listDistinctEntitiesWithInnerFetchPath("payments");

        assertEquals(1, list.size());
    }
 
 
    //    @Test
//    void testSample() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
////        entityManager.flush();
////        entityManager.clear(); // detach all entities
//
//        List<User> list = sample();
//
////        PersistenceUnitUtil util
////                = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
////
////        Assertions.assertTrue(util.isLoaded(list.get(0), "address"));
//
//        assertEquals(2, list.size());
//    }
}
