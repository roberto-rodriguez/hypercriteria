package io.hypercriteria.select.count;

import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.Payment;
import io.sample.model.State;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectCountTest extends BaseTest {

    protected UserDAO userDAO;

    private static final User USER_1 = User.builder()
            .firstName("John")
            .lastName("Smith")
            .address(
                    Address.builder()
                            .street("123 Main Street")
                            .city("Atlanta")
                            .state(
                                    State.builder()
                                            .code("GA")
                                            .name("Georgia").build()
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

    abstract Long countByProperty(String fieldPath);

    abstract Long countDistinctByProperty(String fieldPath);

    abstract Long countEntity();

    abstract Long countUsersFromPayments();

    abstract Long countDistinctUsersFromPayments();

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testCountProperty() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_1);

        long result = countByProperty("firstName");

        assertEquals(2, result);
    }

    @Test
    void testCountDistinctProperty() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_1);

        long result = countDistinctByProperty("firstName");

        assertEquals(1, result);
    }

    @Test
    void testCountNestedProperty() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        long result = countByProperty("payments.amount");

        assertEquals(4, result);
    }

    @Test
    void testCountDistinctNestedProperty() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        long result = countDistinctByProperty("payments.amount");

        assertEquals(2, result);
    }

    @Test
    void testCountEntity() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_1);

        long result = countEntity();

        assertEquals(2, result);
    }

    @Test
    void testCountDistinct_nestedEntity() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        long result = countDistinctUsersFromPayments();//Internally this is SELECT COUNT(DISTINCT user.id)

        assertEquals(1, result);
    }

    @Test
    void testCount_nestedEntity() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        long result = countUsersFromPayments();//Internally this is SELECT COUNT(DISTINCT user.id)

        assertEquals(2, result);
    }

}
