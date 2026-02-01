package io.fluentcriteria.expression.count;

import io.fluentcriteria.FluentCriteria;
import static io.fluentcriteria.FluentCriteria.count;
import static io.fluentcriteria.FluentCriteria.countDistinct;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectCountUsingHyperCriteriaTest extends BaseSelectCountTest {

    @Override
    public Long countByProperty(String fieldPath) {
        return (Long) FluentCriteria.using(entityManager)
                .select(count(fieldPath))
                .from(User.class) 
                .getSingleResult();
    }

    @Override
    public Long countDistinctByProperty(String fieldPath) {
        return (Long) FluentCriteria.using(entityManager)
                .select(countDistinct(fieldPath))
                .from(User.class)
                .getSingleResult();
    }

    @Override
    public Long countEntity() {
        return (Long) FluentCriteria.using(entityManager)
                .select(count())
                .from(User.class)
                .getSingleResult();
    }

    @Override
    Long countUsersFromPayments() {
        return (Long) FluentCriteria.using(entityManager)
                .select(count("user"))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Long countDistinctUsersFromPayments() {
        return (Long) FluentCriteria.using(entityManager)
                .select(countDistinct("user"))
                .from(Payment.class)
                .getSingleResult();
    }

}
