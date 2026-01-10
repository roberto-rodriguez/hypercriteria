package io.hypercriteria.select.abs;

import io.hypercriteria.Criteria;
import io.hypercriteria.Projections;
import static io.hypercriteria.Projections.sum;
import io.hypercriteria.criterion.projection.base.SimpleProjection;
import io.sample.model.Payment;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAbsUsingDAOTest extends BaseSelectAbsTest {

    @Override
    <T extends Number> List<T> absByProperty(String propertyName, Class<T> resultType) {
        return paymentDAO.abs(propertyName, resultType).list();
    }

    @Override
    <T extends Number> List<T> absByNestedProperty(String propertyName, Class<T> resultType) {
        return userDAO.abs(propertyName, resultType).list();
    }

//    @Override
//    <T extends Number> T absSumByProperty(String propertyName, Class<T> resultType) {
//        return (T) userDAO.abs(
//                sum(propertyName, resultType)
//        ).getSingleResult();
//    }

}
