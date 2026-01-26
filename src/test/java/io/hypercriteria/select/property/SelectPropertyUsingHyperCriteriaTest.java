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
    public Object selectProperty(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .getSingleResult();
    }

    @Override
    public Object selectNestedPropertyOneLevel_inplicitJoin(String fieldPath) {
        return selectProperty(fieldPath);
    }

    @Override
    public Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .getSingleResult();
    }

    @Override
    public Object selectNestedPropertyTwoLevels_inplicitJoin(String fieldPath) {
        return selectProperty(fieldPath);
    }

    @Override
    public Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getSingleResult();
    }

    @Override
    public List<String> listProperty(String fieldPath) {
        List<String> list = HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .getResultList(String.class);

        return list;
    }
//
//
//    @Override
//    public List<String> listByPropertyWithInnerJoin(String fieldPath) {
//        return HyperCriteria.using(entityManager)
//                .select(fieldPath)
//                .from(User.class)
//                .innerJoin("address", "a")
//                .getResultList();
//    }
//    
//    
//    @Override
//    public List<String> listByPropertyWithInnerJoin_withRootAlias(String fieldPath) {
//        return HyperCriteria.using(entityManager)
//                .select(fieldPath)
//                .from(User.class, "u")
//                .innerJoin("u.address", "a")
//                .getResultList();
//    }
//
//    @Override
//    public List<String> listByPropertyWithInnerJoin(String fieldPath) {
//        return HyperCriteria.using(entityManager)
//                .select(fieldPath)
//                .from(User.class, "u")
//                .innerJoin("u.address", "a")
//                .getResultList();
//    }
//
//    @Override
//    public List<String> listDistinctByProperty(String fieldPath) {
//        return HyperCriteria.using(entityManager)
//                .select(fieldPath)
//                .distinct()
//                .from(User.class)
//                .getResultList();
//    }
}
