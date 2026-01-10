package io.hypercriteria.select.count;

import static io.hypercriteria.HyperCriteria.count;
import static io.hypercriteria.HyperCriteria.countDistinct;
import io.sample.dao.PaymentDAO;

/**
 *
 * @author rrodriguez
 */
class SelectCountUsingDAOTest extends BaseSelectCountTest {

    @Override
    public Long countByProperty(String fieldPath) {
        return (Long) userDAO.select(count(fieldPath))
                .getSingleResult();
    }

    @Override
    public Long countDistinctByProperty(String fieldPath) {
        return (Long) userDAO.select(countDistinct(fieldPath))
                .getSingleResult();
    }

    @Override
    public Long countEntity() {
        return (Long) userDAO.select(count())
                .getSingleResult();
    }

    @Override
    Long countUsersFromPayments() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);

        return (Long) paymentDAO.select(count("user"))
                .getSingleResult();
    }

    @Override
    Long countDistinctUsersFromPayments() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);

        return (Long) paymentDAO.select(countDistinct("user"))
                .getSingleResult();
    }

}
