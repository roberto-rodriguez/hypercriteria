package io.hypercriteria.select.min;

import static io.hypercriteria.HyperCriteria.min;

/**
 *
 * @author rrodriguez
 */
class SelectMinUsingDAOTest extends BaseSelectMinTest {

    @Override
    Object minByProperty(String fieldPath) {
        return paymentDAO.select(min(fieldPath))
                .getSingleResult();
    }

    @Override
    Object minByNestedProperty(String fieldPath) {
        return userDAO.select(min(fieldPath))
                .getSingleResult();
    }

}
