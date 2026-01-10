package io.hypercriteria.select.abs;

import static io.hypercriteria.HyperCriteria.abs;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAbsUsingDAOTest extends BaseSelectAbsTest {

    @Override
    <T extends Number> List<T> absByProperty(String propertyName, Class<T> resultType) {
        return paymentDAO.select(abs(propertyName, resultType)).list();
    }

    @Override
    <T extends Number> List<T> absByNestedProperty(String propertyName, Class<T> resultType) {
        return userDAO.select(abs(propertyName, resultType)).list();
    }

//    @Override
//    <T extends Number> T absSumByProperty(String propertyName, Class<T> resultType) {
//        return (T) userDAO.abs(
//                sum(propertyName, resultType)
//        ).getSingleResult();
//    }

}
