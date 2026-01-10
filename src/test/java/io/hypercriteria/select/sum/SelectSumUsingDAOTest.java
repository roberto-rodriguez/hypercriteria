package io.hypercriteria.select.sum;

import static io.hypercriteria.HyperCriteria.sum;

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingDAOTest extends BaseSelectSumTest {

    @Override
    Object sumByProperty(String fieldPath) {
        return paymentDAO.select(sum(fieldPath))
                .getSingleResult();
    }

    @Override
    Object sumByNestedProperty(String fieldPath) {
        return userDAO.select(sum(fieldPath))
                .getSingleResult();
    }

}
