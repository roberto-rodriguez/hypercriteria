package io.hypercriteria.select.property;

import io.hypercriteria.HyperCriteria;  
import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectPropertyUsingHyperCriteriaTest extends BaseSelectPropertyTest {
    
    @Override
    public Object selectByProperty(String propertyName) {
        return HyperCriteria.using(entityManager)
                .select(propertyName)
                .from(User.class)
                .getSingleResult();
    }
    
    @Override
    public List<String> listByProperty(String propertyName) {
        return HyperCriteria.using(entityManager)
                .select(propertyName)
                .from(User.class) 
                .list();
    }
    
    @Override
    public List<String> listDistinctByProperty(String propertyName){
        return HyperCriteria.using(entityManager)
                .select(propertyName)
                .distinct()
                .from(User.class) 
                .list();
    }
}
