package io.hypercriteria.select.avg;

import io.sample.dao.PaymentDAO;
import io.sample.dao.UserDAO;
import io.sample.model.User;
import io.utility.BaseTest;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    abstract Double avgByProperty(String propertyName);

    abstract Double avgByNestedProperty(String propertyName);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testSumInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("intValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("longValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("floatValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("doubleValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("bigInteger");
        assertEquals(1.5, result);
    }

    @Test
    void testSumBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByProperty("bigDecimal");
        assertEquals(1.5, result);
    }

    // Nested property 
    @Test
    void testSumNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.intValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.longValue");
        assertEquals(1.5, result);
    }

    @Test
    void testSumNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.floatValue");
        assertEquals(1.5F, result);
    }

    @Test
    void testSumNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.doubleValue");
        assertEquals(1.5D, result);
    }

    @Test
    void testSumNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.bigInteger");
        assertEquals(1.5, result);
    }

    @Test
    void testSumNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = avgByNestedProperty("payments.bigDecimal");
        assertEquals(1.5, result);
    }

}
