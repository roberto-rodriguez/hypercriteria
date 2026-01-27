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
    public Object selectNestedPropertyOneLevel_implicitJoin(String fieldPath) {
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
    public Object selectNestedPropertyTwoLevels_implicitJoin(String fieldPath) {
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

    @Override
    public List<String> listProperty_distinct(String fieldPath) {
        List<String> list = HyperCriteria.using(entityManager)
                .select(fieldPath)
                .distinct()
                .from(User.class)
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listNestedPropertyOneLevel_implicitJoin(String fieldPath) {
        return listProperty(fieldPath);
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitInnerJoin(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins(String fieldPath) {
        return listProperty(fieldPath);
    }

    @Override
    public List<String> testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "role")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedPropertyTwoLevels_explicitLeftJoins(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_distinct(String fieldPath) {
        return listProperty_distinct(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_explicitLeftJoins(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        return HyperCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .innerJoin("a.state", "s")
                .getResultList();
    }
}
