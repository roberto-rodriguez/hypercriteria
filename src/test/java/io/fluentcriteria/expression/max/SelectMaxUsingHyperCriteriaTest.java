package io.fluentcriteria.expression.max;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.max;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingHyperCriteriaTest extends BaseSelectMaxTest {

    @Override
    Object maxByProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(max(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object maxByNestedProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(max(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
