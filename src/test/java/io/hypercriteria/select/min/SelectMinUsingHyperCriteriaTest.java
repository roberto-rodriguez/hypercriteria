package io.hypercriteria.select.min;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.min;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMinUsingHyperCriteriaTest extends BaseSelectMinTest {

    @Override
    Object minByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(min(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object minByNestedProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(min(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
