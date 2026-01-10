package io.hypercriteria.select.abs;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.abs;
import static io.hypercriteria.Projections.sum;
import io.sample.model.Payment;
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAbsUsingHyperCriteriaTest extends BaseSelectAbsTest {

    @Override
    <T extends Number> List<T> absByProperty(String propertyName, Class<T> resultType) {
        return HyperCriteria.using(entityManager)
                .select(abs(propertyName, resultType))
                .from(Payment.class)
                .list();
    }

    @Override
    <T extends Number> List<T> absByNestedProperty(String propertyName, Class<T> resultType) {
        return HyperCriteria.using(entityManager)
                .select(abs(propertyName, resultType))
                .from(User.class)
                .list();
    }

//    @Override
//    <T extends Number> T absSumByProperty(String propertyName, Class<T> resultType) {
//        return (T) HyperCriteria.using(entityManager)
//                .abs(
//                        sum(propertyName, resultType) 
//                )
//                .from(Payment.class)
//                .getSingleResult();
//    }
}
