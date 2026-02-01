package io.fluentcriteria.expression.min;

import static io.fluentcriteria.FluentCriteria.min;

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
