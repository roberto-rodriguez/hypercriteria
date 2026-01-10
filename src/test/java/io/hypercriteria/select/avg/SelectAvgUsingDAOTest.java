package io.hypercriteria.select.avg;

import static io.hypercriteria.HyperCriteria.avg;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingDAOTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String fieldPath) {
        return (Double) paymentDAO.select(avg(fieldPath))
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String fieldPath) {
        return (Double) userDAO.select(avg(fieldPath))
                .getSingleResult();
    }

}
