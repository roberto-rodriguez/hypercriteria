package io.hypercriteria.select.min;

import static io.hypercriteria.HyperCriteria.min;

/**
 *
 * @author rrodriguez
 */
class SelectMinUsingDAOTest extends BaseSelectMinTest {

    @Override
    <T extends Number> T minByProperty(String propertyName, Class<T> resultType) {
        return (T) paymentDAO.select(min(propertyName, resultType))
                .getSingleResult();
    }

    @Override
    <T extends Number> T minByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) userDAO.select(min(propertyName, resultType))
                .getSingleResult();
    }

}
