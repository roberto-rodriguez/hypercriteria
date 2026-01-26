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

    abstract Object minByProperty(String fieldPath);

    abstract Object minByNestedProperty(String fieldPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testMinInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = (Integer) minByProperty("intValue");
        assertEquals(1, result);
    }

    @Test
    void testMinLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) minByProperty("longValue");
        assertEquals(1L, result);
    }

    @Test
    void testMinFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = (Float) minByProperty("floatValue");
        assertEquals(1F, result);
    }

    @Test
    void testMinDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) minByProperty("doubleValue");
        assertEquals(1D, result);
    }

    @Test
    void testMinBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) minByProperty("bigInteger");
        assertEquals(new BigInteger("1"), result);
    }

    @Test
    void testMinBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) minByProperty("bigDecimal");
        assertEquals(new BigDecimal("1.00"), result);
    }

    // Nested property 
    @Test
    void testMinNestedInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Integer result = (Integer) minByNestedProperty("payments.intValue");
        assertEquals(1, result);
    }

    @Test
    void testMinNestedLong() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Long result = (Long) minByNestedProperty("payments.longValue");
        assertEquals(1L, result);
    }

    @Test
    void testMinNestedFloat() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Float result = (Float) minByNestedProperty("payments.floatValue");
        assertEquals(1F, result);
    }

    @Test
    void testMinNestedDouble() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        Double result = (Double) minByNestedProperty("payments.doubleValue");
        assertEquals(1D, result);
    }

    @Test
    void testMinNestedBigInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigInteger result = (BigInteger) minByNestedProperty("payments.bigInteger");
        assertEquals(new BigInteger("1"), result);
    }

    @Test
    void testMinNestedBigDecimal() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        BigDecimal result = (BigDecimal) minByNestedProperty("payments.bigDecimal");
        assertEquals(new BigDecimal("1.00"), result);
    }

}
