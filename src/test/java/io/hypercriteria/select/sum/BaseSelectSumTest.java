package io.hypercriteria.select.sum;

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
abstract class BaseSelectSumTest extends BaseTest {

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

    abstract Object sumByProperty(String fieldPath);

    abstract Object sumByNestedProperty(String fieldPath);

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
        Long result = (Long) sumByProperty("intValue");
        assertEquals(3L, result);
    }

    @Test
    void testSumLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) sumByProperty("longValue");
        assertEquals(3L, result);
    }

    @Test
    void testSumFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) sumByProperty("floatValue");
        assertEquals(3D, result);
    }

    @Test
    void testSumDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) sumByProperty("doubleValue");
        assertEquals(3D, result);
    }

    @Test
    void testSumBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) sumByProperty("bigInteger");
        assertEquals(new BigInteger("3"), result);
    }

    @Test
    void testSumBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) sumByProperty("bigDecimal");
        assertEquals(new BigDecimal("3.00"), result);
    }

    // Nested property 
    @Test
    void testSumNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) sumByNestedProperty("payments.intValue");
        assertEquals(3L, result);
    }

    @Test
    void testSumNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) sumByNestedProperty("payments.longValue");
        assertEquals(3L, result);
    }

    @Test
    void testSumNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) sumByNestedProperty("payments.floatValue");
        assertEquals(3D, result);
    }

    @Test
    void testSumNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) sumByNestedProperty("payments.doubleValue");
        assertEquals(3D, result);
    }

    @Test
    void testSumNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) sumByNestedProperty("payments.bigInteger");
        assertEquals(new BigInteger("3"), result);
    }

    @Test
    void testSumNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) sumByNestedProperty("payments.bigDecimal");
        assertEquals(new BigDecimal("3.00"), result);
    }
}
