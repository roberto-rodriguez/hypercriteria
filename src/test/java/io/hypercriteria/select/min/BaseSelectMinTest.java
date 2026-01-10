package io.hypercriteria.select.min;

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
abstract class BaseSelectMinTest extends BaseTest {

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

    abstract <T extends Number> T minByProperty(String propertyName, Class<T> resultType);

    abstract <T extends Number> T minByNestedProperty(String propertyName, Class<T> resultType);

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
        Integer result = minByProperty("intValue", Integer.class);
        assertEquals(1, result);
    }

    @Test
    void testSumLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = minByProperty("longValue", Long.class);
        assertEquals(1L, result);
    }

    @Test
    void testSumFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = minByProperty("floatValue", Float.class);
        assertEquals(1F, result);
    }

    @Test
    void testSumDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = minByProperty("doubleValue", Double.class);
        assertEquals(1D, result);
    }

    @Test
    void testSumBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = minByProperty("bigInteger", BigInteger.class);
        assertEquals(new BigInteger("1"), result);
    }

    @Test
    void testSumBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = minByProperty("bigDecimal", BigDecimal.class);
        assertEquals(new BigDecimal("1.00"), result);
    }

    // Nested property 
    @Test
    void testSumNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = minByNestedProperty("payments.intValue", Integer.class);
        assertEquals(1, result);
    }

    @Test
    void testSumNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = minByNestedProperty("payments.longValue", Long.class);
        assertEquals(1L, result);
    }

    @Test
    void testSumNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = minByNestedProperty("payments.floatValue", Float.class);
        assertEquals(1F, result);
    }

    @Test
    void testSumNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = minByNestedProperty("payments.doubleValue", Double.class);
        assertEquals(1D, result);
    }

    @Test
    void testSumNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = minByNestedProperty("payments.bigInteger", BigInteger.class);
        assertEquals(new BigInteger("1"), result);
    }

    @Test
    void testSumNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = minByNestedProperty("payments.bigDecimal", BigDecimal.class);
        assertEquals(new BigDecimal("1.00"), result);
    }

}
