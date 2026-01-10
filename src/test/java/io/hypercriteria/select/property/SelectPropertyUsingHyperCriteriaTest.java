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
    public Object selectByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .getSingleResult();
    }
    
    @Override
    public List<String> listByProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class) 
                .getResultList();
    }
    
    @Override
    public List<String> listDistinctByProperty(String fieldPath){
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .distinct()
                .from(User.class) 
                .getResultList();
    }
}
