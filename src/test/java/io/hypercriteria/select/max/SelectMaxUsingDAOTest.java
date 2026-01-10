package io.hypercriteria.select.max;

import static io.hypercriteria.HyperCriteria.max;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingDAOTest extends BaseSelectMaxTest {

    @Override
    <T extends Number> T maxByProperty(String propertyName, Class<T> resultType) {
        return (T) paymentDAO.select(max(propertyName, resultType))
                .getSingleResult();
    }

    @Override
    <T extends Number> T maxByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) userDAO.select(max(propertyName, resultType))
                .getSingleResult();
    }

}
