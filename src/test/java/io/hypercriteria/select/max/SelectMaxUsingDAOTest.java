package io.hypercriteria.select.max;

import static io.hypercriteria.HyperCriteria.max;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingDAOTest extends BaseSelectMaxTest {

    @Override
    Object maxByProperty(String fieldPath) {
        return paymentDAO.select(max(fieldPath))
                .getSingleResult();
    }

    @Override
    Object maxByNestedProperty(String fieldPath) {
        return userDAO.select(max(fieldPath))
                .getSingleResult();
    }

}
