package io.hypercriteria.select.count;
 
import io.hypercriteria.HyperCriteria;
import io.sample.model.Payment;
import io.sample.model.User; 

/**
 *
 * @author rrodriguez
 */
class SelectCountUsingHyperCriteriaTest extends BaseSelectCountTest {

    @Override
    public Long countByProperty(String propertyName) {
        return (Long) HyperCriteria.using(entityManager)
                .count(propertyName)
                .from(User.class)
                .getSingleResult();
    }

    @Override
    public Long countDistinctByProperty(String propertyName) {
        return (Long) HyperCriteria.using(entityManager)
                .countDistinct(propertyName)
                .from(User.class)
                .getSingleResult();
    }

    @Override
    public Long countEntity() {
        return (Long) HyperCriteria.using(entityManager)
                .count()
                .from(User.class)
                .getSingleResult();
    }
 
    @Override
    Long countUsersFromPayments() {
           return (Long) HyperCriteria.using(entityManager)
                .count("user")
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Long countDistinctUsersFromPayments() {
           return (Long) HyperCriteria.using(entityManager)
                .countDistinct("user")
                .from(Payment.class)
                .getSingleResult();
    }

}
