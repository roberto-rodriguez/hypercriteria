package io.hypercriteria.select.max;
   
import io.hypercriteria.HyperCriteria;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingHyperCriteriaTest extends BaseSelectMaxTest {

    @Override
    <T extends Number> T maxByProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .max(propertyName, resultType)
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    <T extends Number> T maxByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .max(propertyName, resultType)
                .from(User.class)
                .getSingleResult();
    }

}
