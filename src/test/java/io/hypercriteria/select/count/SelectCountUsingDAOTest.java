package io.hypercriteria.select.count;

import io.sample.dao.PaymentDAO;

/**
 *
 * @author rrodriguez
 */
class SelectCountUsingDAOTest extends BaseSelectCountTest {

    @Override
    public Long countByProperty(String propertyName) {
        return (Long) userDAO.count(propertyName)
                .getSingleResult();
    }

    @Override
    public Long countDistinctByProperty(String propertyName) {
        return (Long) userDAO.countDistinct(propertyName)
                .getSingleResult();
    }

    @Override
    public Long countEntity() {
        return (Long) userDAO.count()
                .getSingleResult();
    }
 
    @Override
    Long countUsersFromPayments() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);
        
      return (Long) paymentDAO.count("user")
                .getSingleResult();     
    }

    @Override
    Long countDistinctUsersFromPayments() {
        PaymentDAO paymentDAO = new PaymentDAO();
        paymentDAO.setEntityManager(entityManager);
        
      return (Long) paymentDAO.countDistinct("user")
                .getSingleResult();     
    }

   
}
