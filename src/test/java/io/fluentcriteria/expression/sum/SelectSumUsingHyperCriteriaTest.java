package io.fluentcriteria.expression.sum;

import io.hypercriteria.FluentCriteria;
import static io.hypercriteria.FluentCriteria.sum;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingHyperCriteriaTest extends BaseSelectSumTest {

    @Override
    Object sumByProperty(String fieldPath ) {
        return FluentCriteria.using(entityManager)
                .select(sum(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
 Object sumByNestedProperty(String fieldPath ) {
        return  FluentCriteria.using(entityManager)
                .select(sum(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
