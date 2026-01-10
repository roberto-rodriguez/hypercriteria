package io.hypercriteria.select.avg;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingDAOTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String propertyName) {
        return (Double) paymentDAO.avg(propertyName)
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String propertyName) {
        return (Double) userDAO.avg(propertyName)
                .getSingleResult();
    }

}
