package io.hypercriteria.select.avg;

import io.hypercriteria.HyperCriteria;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingHyperCriteriaTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String propertyName) {
        return (Double) HyperCriteria.using(entityManager)
                .avg(propertyName)
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String propertyName) {
        return (Double) HyperCriteria.using(entityManager)
                .avg(propertyName)
                .from(User.class)
                .getSingleResult();
    }

}
