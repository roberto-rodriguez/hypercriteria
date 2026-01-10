package io.hypercriteria.select.abs;

import io.sample.dao.PaymentDAO;
import io.sample.dao.UserDAO;
import io.sample.model.User;
import io.utility.BaseTest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectAbsTest extends BaseTest {

    protected UserDAO userDAO;
    protected PaymentDAO paymentDAO;

    private static final User USER_WITH_PAYMENTS = User.builder()
            .firstName("John")
            .lastName("Smith")
            .payments(new ArrayList<>())
            .build();

    static {
        USER_WITH_PAYMENTS.addPaymentWithNumericValues(-1);
        USER_WITH_PAYMENTS.addPaymentWithNumericValues(-2);
    }

    abstract List absByProperty(String fieldPath);

    abstract List absByNestedProperty(String fieldPath);

    //Sum then apply abs
//    abstract <T extends Number, R extends Number> R absSumByProperty(String fieldPath, Class<T> resultType);
    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

//    @Test
//    void testAbsSumInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Long result = absSumByProperty("intValue", Integer.class);
//
//        assertEquals(3, result);
//    }
    @Test
    void testAbsInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Integer> result = absByProperty("intValue");

        assertEquals(Arrays.asList(1, 2), result);
    }

    @Test
    void testAbsLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Long> result = absByProperty("longValue");
        assertEquals(Arrays.asList(1L, 2L), result);
    }

    @Test
    void testAbsFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Float> result = absByProperty("floatValue");
        assertEquals(Arrays.asList(1F, 2F), result);
    }

    @Test
    void testAbsDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Double> result = absByProperty("doubleValue");
        assertEquals(Arrays.asList(1D, 2D), result);
    }

    @Test
    void testAbsBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<BigInteger> result = absByProperty("bigInteger");
        assertEquals(Arrays.asList(new BigInteger("1"), new BigInteger("2")), result);
    }

    @Test
    void testAbsBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<BigDecimal> result = absByProperty("bigDecimal");
        assertEquals(Arrays.asList(new BigDecimal("1.00"), new BigDecimal("2.00")), result);
    }

    // Nested property 
    @Test
    void testAbsNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Long> result = absByNestedProperty("payments.longValue");
        assertEquals(Arrays.asList(1L, 2L), result);
    }

    @Test
    void testAbsNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Float> result = absByNestedProperty("payments.floatValue");
        assertEquals(Arrays.asList(1F, 2F), result);
    }

    @Test
    void testAbsNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Double> result = absByNestedProperty("payments.doubleValue");
        assertEquals(Arrays.asList(1D, 2D), result);
    }

    @Test
    void testAbsNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<BigInteger> result = absByNestedProperty("payments.bigInteger");
        assertEquals(Arrays.asList(new BigInteger("1"), new BigInteger("2")), result);
    }

    @Test
    void testAbsNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<BigDecimal> result = absByNestedProperty("payments.bigDecimal");
        assertEquals(Arrays.asList(new BigDecimal("1.00"), new BigDecimal("2.00")), result);
    }

}
