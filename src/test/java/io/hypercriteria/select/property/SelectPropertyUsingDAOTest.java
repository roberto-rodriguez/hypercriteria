package io.hypercriteria.select.property;
 
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectPropertyUsingDAOTest extends BaseSelectPropertyTest {

    @Override
    public Object selectByProperty(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getSingleResult();
    }

    @Override
    public List<String> listByProperty(String fieldPath) {
        return userDAO
                .select(fieldPath) 
                .getResultList();
    }

    @Override
    public List<String> listDistinctByProperty(String fieldPath) {
        return userDAO
                .select(fieldPath) 
                .distinct()
                .getResultList();
    }
}
