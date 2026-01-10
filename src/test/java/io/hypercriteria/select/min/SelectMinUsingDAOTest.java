package io.hypercriteria.select.min;

 
/**
 *
 * @author rrodriguez
 */
class SelectMinUsingDAOTest extends BaseSelectMinTest {

    @Override
    <T extends Number> T minByProperty(String propertyName, Class<T> resultType) { 
        return (T) paymentDAO.min(propertyName, resultType)
                .getSingleResult();
    }

    @Override
    <T extends Number> T minByNestedProperty(String propertyName, Class<T> resultType) { 
        return (T) userDAO.min(propertyName, resultType)
                .getSingleResult();
    }
    

}
