package io.hypercriteria.select.max;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.max;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingHyperCriteriaTest extends BaseSelectMaxTest {

    @Override
    Object maxByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(max(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object maxByNestedProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(max(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
