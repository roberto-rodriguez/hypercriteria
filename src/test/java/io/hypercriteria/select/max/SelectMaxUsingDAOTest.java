package io.hypercriteria.select.max;
 
/**
 *
 * @author rrodriguez
 */
class SelectMaxUsingDAOTest extends BaseSelectMaxTest {

    @Override
    <T extends Number> T maxByProperty(String propertyName, Class<T> resultType) { 
        return (T) paymentDAO.max(propertyName, resultType)
                .getSingleResult();
    }

    @Override
    <T extends Number> T maxByNestedProperty(String propertyName, Class<T> resultType) { 
        return (T) userDAO.max(propertyName, resultType)
                .getSingleResult();
    }
    

}
