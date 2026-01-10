package io.hypercriteria.select.sum;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.sum;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingHyperCriteriaTest extends BaseSelectSumTest {

    @Override
    Object sumByProperty(String fieldPath ) {
        return HyperCriteria.using(entityManager)
                .select(sum(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
 Object sumByNestedProperty(String fieldPath ) {
        return  HyperCriteria.using(entityManager)
                .select(sum(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
