package io.hypercriteria.select.abs;

import io.hypercriteria.HyperCriteria;
import static io.hypercriteria.HyperCriteria.abs;
import static io.hypercriteria.HyperCriteria.sum;
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

    @Override
    Object absSumByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(abs(sum(fieldPath)))
                .from(Payment.class)
                .getSingleResult();
    }

    @Override
    Object sumAbsByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(sum(abs(fieldPath)))
                .from(Payment.class)
                .getSingleResult();
    }
}
