package io.hypercriteria.select.property;

import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectPropertyUsingDAOTest extends BaseSelectPropertyTest {

    @Override
    public Object selectProperty(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getSingleResult();
    }

    @Override
    public Object selectNestedPropertyOneLevel_inplicitJoin(String fieldPath) {
        return selectProperty(fieldPath);
    }

    @Override
    public Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getSingleResult();
    }

    @Override
    public Object selectNestedPropertyTwoLevels_inplicitJoin(String fieldPath) {
        return selectProperty(fieldPath);
    }

    @Override
    public Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getSingleResult();
    }

    @Override
    public List<String> listProperty(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getResultList();
    }
//    
//    @Override
//    public List<String> listByPropertyWithInnerJoin(String fieldPath) {
//        return userDAO
//                .select(fieldPath)
//                .innerJoin("address", "a")
//                .getResultList();
//    }
//
//    @Override
//    public List<String> listByPropertyWithInnerJoin_withRootAlias(String fieldPath) {
//        return userDAO
//                .select(fieldPath)
//                .from(User.class, "u")
//                .innerJoin("u.address", "a")
//                .getResultList();
//    }
// 
//    @Override
//    public List<String> listDistinctByProperty(String fieldPath) {
//        return userDAO
//                .select(fieldPath) 
//                .distinct()
//                .getResultList();
//    }
}
