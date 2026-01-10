package io.hypercriteria.select.sum;

import static io.hypercriteria.HyperCriteria.sum;

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingDAOTest extends BaseSelectSumTest {

    @Override
    <T extends Number> T sumByProperty(String propertyName, Class<T> resultType) {
        return (T) paymentDAO.select(sum(propertyName, resultType))
                .getSingleResult();
    }

    @Override
    <T extends Number> T sumByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) userDAO.select(sum(propertyName, resultType))
                .getSingleResult();
    }

}
