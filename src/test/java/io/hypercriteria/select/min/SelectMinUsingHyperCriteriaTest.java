package io.hypercriteria.select.min;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.min;
import io.sample.model.Payment;
import io.sample.model.User;

/**
 *
 * @author rrodriguez
 */
class SelectMinUsingHyperCriteriaTest extends BaseSelectMinTest {

    @Override
    <T extends Number> T minByProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .select(min(propertyName, resultType))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    <T extends Number> T minByNestedProperty(String propertyName, Class<T> resultType) {
        return (T) HyperCriteria.using(entityManager)
                .select(min(propertyName, resultType))
                .from(User.class)
                .getSingleResult();
    }

}
