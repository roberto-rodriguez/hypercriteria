package io.hypercriteria.select.abs;

import static io.hypercriteria.HyperCriteria.abs;
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

//    @Override
//    <T extends Number> T absSumByProperty(String fieldPath, Class<T> resultType) {
//        return (T) userDAO.abs(
//                sum(fieldPath, resultType)
//        ).getSingleResult();
//    }
}
