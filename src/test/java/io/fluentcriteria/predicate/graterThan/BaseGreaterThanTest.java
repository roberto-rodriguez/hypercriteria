package io.fluentcriteria.predicate.graterThan;

import io.sample.dao.PaymentDAO;
import io.sample.dao.UserDAO;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
public abstract class BaseGreaterThanTest extends BaseTest {

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

    abstract <T extends Comparable<T>> List<T> greaterThanByProperty(String fieldPath, T value);
//    abstract <T extends Comparable<T>> List<T> greaterThanByNestedProperty(String fieldPath, T value);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually

        paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);  // assign manually
    }

    @Test
    void testGreaterThanInteger() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
        List<Integer> result = greaterThanByProperty("intValue", 1);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), 2);
    }

//    @Test
//    void testGreaterThanLong() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Long result = (Long) greaterThanByProperty("longValue");
//        assertEquals(2L, result);
//    }
//
//    @Test
//    void testGreaterThanFloat() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Float result = (Float) greaterThanByProperty("floatValue");
//        assertEquals(2F, result);
//    }
//
//    @Test
//    void testGreaterThanDouble() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        Double result = (Double) greaterThanByProperty("doubleValue");
//        assertEquals(2D, result);
//    }
//
//    @Test
//    void testGreaterThanBigInteger() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigInteger result = (BigInteger) greaterThanByProperty("bigInteger");
//        assertEquals(new BigInteger("2"), result);
//    }
//
//    @Test
//    void testGreaterThanBigDecimal() {
//        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);
//        BigDecimal result = (BigDecimal) greaterThanByProperty("bigDecimal");
//        assertEquals(new BigDecimal("2.00"), result);
//    }
}
