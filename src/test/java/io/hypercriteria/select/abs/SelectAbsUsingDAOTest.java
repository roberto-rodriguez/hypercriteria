package io.hypercriteria.select.abs;

import static io.hypercriteria.HyperCriteria.abs;
import static io.hypercriteria.HyperCriteria.sum;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAbsUsingDAOTest extends BaseSelectAbsTest {

    @Override
    List absByProperty(String fieldPath) {
        return paymentDAO.select(abs(fieldPath)).getResultList();
    }

    @Override
    List absByNestedProperty(String fieldPath) {
        return userDAO.select(abs(fieldPath)).getResultList();
    }

    @Override
    Object absSumByProperty(String fieldPath) {
        return paymentDAO.select(abs(sum(fieldPath))).getSingleResult();
    }

    @Override
    Object sumAbsByProperty(String fieldPath) {
        return paymentDAO.select(sum(abs(fieldPath))).getSingleResult();
    }
}
