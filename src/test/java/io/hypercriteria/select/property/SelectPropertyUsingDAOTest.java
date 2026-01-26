package io.hypercriteria.select.property;
 
import io.sample.model.User;
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
    public List<String> listByPropertyWithInnerJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    public List<String> listByPropertyWithInnerJoin_withRootAlias(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .from(User.class, "u")
                .innerJoin("u.address", "a")
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
