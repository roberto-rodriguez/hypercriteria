package io.hypercriteria.select.max;

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
abstract class BaseSelectMaxTest extends BaseTest {

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

    abstract <T extends Number> T maxByProperty(String propertyName, Class<T> resultType);

    abstract <T extends Number> T maxByNestedProperty(String propertyName, Class<T> resultType);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testMaxInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = maxByProperty("intValue", Integer.class);
        assertEquals(2, result);
    }

    @Test
    void testMaxLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = maxByProperty("longValue", Long.class);
        assertEquals(2L, result);
    }

    @Test
    void testMaxFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = maxByProperty("floatValue", Float.class);
        assertEquals(2F, result);
    }

    @Test
    void testMaxDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = maxByProperty("doubleValue", Double.class);
        assertEquals(2D, result);
    }

    @Test
    void testMaxBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = maxByProperty("bigInteger", BigInteger.class);
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    void testMaxBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = maxByProperty("bigDecimal", BigDecimal.class);
        assertEquals(new BigDecimal("2.00"), result);
    }

    // Nested property 
    @Test
    void testMaxNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = maxByNestedProperty("payments.intValue", Integer.class);
        assertEquals(2, result);
    }

    @Test
    void testMaxNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = maxByNestedProperty("payments.longValue", Long.class);
        assertEquals(2L, result);
    }

    @Test
    void testMaxNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = maxByNestedProperty("payments.floatValue", Float.class);
        assertEquals(2F, result);
    }

    @Test
    void testMaxNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = maxByNestedProperty("payments.doubleValue", Double.class);
        assertEquals(2D, result);
    }

    @Test
    void testMaxNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = maxByNestedProperty("payments.bigInteger", BigInteger.class);
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    void testMaxNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = maxByNestedProperty("payments.bigDecimal", BigDecimal.class);
        assertEquals(new BigDecimal("2.00"), result);
    }

}
