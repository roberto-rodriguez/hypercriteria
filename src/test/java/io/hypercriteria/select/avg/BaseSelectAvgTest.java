package io.hypercriteria.select.avg;

import io.sample.dao.PaymentDAO;
import io.sample.dao.UserDAO;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectAvgTest extends BaseTest {

    protected UserDAO userDAO;
    protected PaymentDAO paymentDAO;

    private static final User USER_WITH_PAYMENTS = User.builder()
            .firstName("John")
            .lastName("Smith")
            .payments(new ArrayList<>())
            .build();

    static {
        USER_WITH_PAYMENTS.addPaymentWithNumericValues(1);
        USER_WITH_PAYMENTS.addPaymentWithNumericValues(2);
    }

    abstract Double avgByProperty(String fieldPath);

    abstract Double avgByNestedProperty(String fieldPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testAvgInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("intValue");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("longValue");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("floatValue");
        assertEquals(1.5F, result);
    }

    @Test
    void testAvgDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("doubleValue");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("bigInteger");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("bigDecimal");
        assertEquals(1.5, result);
    }

    // Nested property 
    @Test
    void testAvgNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.intValue");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.longValue");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.floatValue");
        assertEquals(1.5F, result);
    }

    @Test
    void testAvgNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.doubleValue");
        assertEquals(1.5D, result);
    }

    @Test
    void testAvgNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.bigInteger");
        assertEquals(1.5, result);
    }

    @Test
    void testAvgNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.bigDecimal");
        assertEquals(1.5, result);
    }

}
