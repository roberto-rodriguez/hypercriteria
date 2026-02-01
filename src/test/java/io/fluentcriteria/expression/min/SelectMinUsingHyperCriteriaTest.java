package io.fluentcriteria.expression.min;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.min;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMinUsingHyperCriteriaTest extends BaseSelectMinTest {

    @Override
    Object minByProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(min(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object minByNestedProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(min(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
