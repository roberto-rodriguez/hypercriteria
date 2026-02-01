package io.fluentcriteria.expression.abs;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.abs;
import static io.fluentcriteria.FluentCriteria.sum;
import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAbsUsingHyperCriteriaTest extends BaseSelectAbsTest {

    @Override
    List absByProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .getResultList();
    }

    @Override
    List absByNestedProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(User.class)
                .getResultList();
    }

    @Override
    Object absSumByProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(abs(sum(fieldPath)))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object sumAbsByProperty(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(sum(abs(fieldPath)))
                .from(Payment.class)
                .getSingleResult();
    }
}
