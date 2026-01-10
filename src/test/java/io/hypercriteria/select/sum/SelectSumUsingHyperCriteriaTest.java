package io.hypercriteria.select.sum;
  
import io.hypercriteria.HyperCriteria;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingHyperCriteriaTest extends BaseSelectSumTest {

    @Override
    <T extends Number> T sumByProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .sum(propertyName, resultType)
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    <T extends Number> T sumByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .sum(propertyName, resultType)
                .from(User.class)
                .getSingleResult();
    }

}
