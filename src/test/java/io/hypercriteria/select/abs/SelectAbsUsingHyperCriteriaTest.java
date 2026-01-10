package io.hypercriteria.select.abs;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.abs;
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
        return HyperCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(Payment.class)
                .getResultList();
    }

    @Override
    List absByNestedProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(abs(fieldPath))
                .from(User.class)
                .getResultList();
    }

//    @Override
//    <T extends Number> T absSumByProperty(String fieldPath, Class<T> resultType) {
//        return (T) HyperCriteria.using(entityManager)
//                .abs(
//                        sum(fieldPath, resultType) 
//                )
//                .from(Payment.class)
//                .getSingleResult();
//    }
}
