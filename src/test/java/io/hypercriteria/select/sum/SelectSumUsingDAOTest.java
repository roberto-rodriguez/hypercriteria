package io.hypercriteria.select.sum;
 

/**
 *
 * @author rrodriguez
 */
class SelectSumUsingDAOTest extends BaseSelectSumTest {

    @Override
    <T extends Number> T sumByProperty(String propertyName, Class<T> resultType) { 
        return (T) paymentDAO.sum(propertyName, resultType)
                .getSingleResult();
    }

    @Override
    <T extends Number> T sumByNestedProperty(String propertyName, Class<T> resultType) { 
        return (T) userDAO.sum(propertyName, resultType)
                .getSingleResult();
    }
    

}
