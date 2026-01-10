package io.hypercriteria.select.avg;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.avg;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingHyperCriteriaTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String fieldPath) {
        return (Double) HyperCriteria.using(entityManager)
                .select(avg(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String fieldPath) {
        return (Double) HyperCriteria.using(entityManager)
                .select(avg(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
