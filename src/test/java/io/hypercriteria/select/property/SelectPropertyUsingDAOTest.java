package io.hypercriteria.select.property;
 
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectPropertyUsingDAOTest extends BaseSelectPropertyTest {

    @Override
    public Object selectByProperty(String propertyName) {
        return userDAO
                .select(propertyName)
                .getSingleResult();
    }

    @Override
    public List<String> listByProperty(String propertyName) {
        return userDAO
                .select(propertyName) 
                .list();
    }

    @Override
    public List<String> listDistinctByProperty(String propertyName) {
        return userDAO
                .select(propertyName) 
                .distinct()
                .list();
    }
}
