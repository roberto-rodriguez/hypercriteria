package io.fluentcriteria.expression.avg;

import io.hypercriteria.FluentCriteria;
import static io.hypercriteria.FluentCriteria.avg;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectAvgUsingHyperCriteriaTest extends BaseSelectAvgTest {

    @Override
    Double avgByProperty(String fieldPath) {
        return (Double) FluentCriteria.using(entityManager)
                .select(avg(fieldPath))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Double avgByNestedProperty(String fieldPath) {
        return (Double) FluentCriteria.using(entityManager)
                .select(avg(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

}
