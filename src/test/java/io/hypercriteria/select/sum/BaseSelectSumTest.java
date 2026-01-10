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

    abstract <T extends Number> T sumByProperty(String propertyName, Class<T> resultType);

    abstract <T extends Number> T sumByNestedProperty(String propertyName, Class<T> resultType);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

//    @Test
//    void testSumInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Integer result = sumByProperty("intValue", Integer.class);
//        assertEquals(3, result);
//    }
//
//    @Test
//    void testSumLong() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Long result = sumByProperty("longValue", Long.class);
//        assertEquals(3L, result);
//    }
//
//    @Test
//    void testSumFloat() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Float result = sumByProperty("floatValue", Float.class);
//        assertEquals(3F, result);
//    }
//
//    @Test
//    void testSumDouble() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Double result = sumByProperty("doubleValue", Double.class);
//        assertEquals(3D, result);
//    }
//
//    @Test
//    void testSumBigInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigInteger result = sumByProperty("bigInteger", BigInteger.class);
//        assertEquals(new BigInteger("3"), result);
//    }
//
//    @Test
//    void testSumBigDecimal() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigDecimal result = sumByProperty("bigDecimal", BigDecimal.class);
//        assertEquals(new BigDecimal("3.00"), result);
//    }
//
//    // Nested property 
//    @Test
//    void testSumNestedInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Integer result = sumByNestedProperty("payments.intValue", Integer.class);
//        assertEquals(3, result);
//    }
//
//    @Test
//    void testSumNestedLong() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Long result = sumByNestedProperty("payments.longValue", Long.class);
//        assertEquals(3L, result);
//    }
//
//    @Test
//    void testSumNestedFloat() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Float result = sumByNestedProperty("payments.floatValue", Float.class);
//        assertEquals(3F, result);
//    }
//
//    @Test
//    void testSumNestedDouble() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Double result = sumByNestedProperty("payments.doubleValue", Double.class);
//        assertEquals(3D, result);
//    }
//
//    @Test
//    void testSumNestedBigInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigInteger result = sumByNestedProperty("payments.bigInteger", BigInteger.class);
//        assertEquals(new BigInteger("3"), result);
//    }
//
//    @Test
//    void testSumNestedBigDecimal() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigDecimal result = sumByNestedProperty("payments.bigDecimal", BigDecimal.class);
//        assertEquals(new BigDecimal("3.00"), result);
//    }

}
