package io.hypercriteria.select.avg;

import static io.hypercriteria.HyperCriteria.avg;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingDAOTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String propertyName) {
        return (Double) paymentDAO.select(avg(propertyName))
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String propertyName) {
        return (Double) userDAO.select(avg(propertyName))
                .getSingleResult();
    }

}
