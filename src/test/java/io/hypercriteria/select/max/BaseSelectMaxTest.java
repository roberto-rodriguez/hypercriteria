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

    abstract Object maxByProperty(String fieldPath);

    abstract Object maxByNestedProperty(String fieldPath);

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
        Integer result = (Integer) maxByProperty("intValue");
        assertEquals(2, result);
    }

    @Test
    void testMaxLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) maxByProperty("longValue");
        assertEquals(2L, result);
    }

    @Test
    void testMaxFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = (Float) maxByProperty("floatValue");
        assertEquals(2F, result);
    }

    @Test
    void testMaxDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) maxByProperty("doubleValue");
        assertEquals(2D, result);
    }

    @Test
    void testMaxBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) maxByProperty("bigInteger");
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    void testMaxBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) maxByProperty("bigDecimal");
        assertEquals(new BigDecimal("2.00"), result);
    }

    // Nested property 
    @Test
    void testMaxNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = (Integer) maxByNestedProperty("payments.intValue");
        assertEquals(2, result);
    }

    @Test
    void testMaxNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) maxByNestedProperty("payments.longValue");
        assertEquals(2L, result);
    }

    @Test
    void testMaxNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = (Float) maxByNestedProperty("payments.floatValue");
        assertEquals(2F, result);
    }

    @Test
    void testMaxNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) maxByNestedProperty("payments.doubleValue");
        assertEquals(2D, result);
    }

    @Test
    void testMaxNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) maxByNestedProperty("payments.bigInteger");
        assertEquals(new BigInteger("2"), result);
    }

    @Test
    void testMaxNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) maxByNestedProperty("payments.bigDecimal");
        assertEquals(new BigDecimal("2.00"), result);
    }

}
