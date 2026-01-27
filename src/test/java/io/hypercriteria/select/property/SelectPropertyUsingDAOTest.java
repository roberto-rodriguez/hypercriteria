package io.hypercriteria.select.property;

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
    public Object selectNestedPropertyOneLevel_implicitJoin(String fieldPath) {
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
    public Object selectNestedPropertyTwoLevels_implicitJoin(String fieldPath) {
        return selectProperty(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedPropertyTwoLevels_explicitLeftJoins(fieldPath);
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

    @Override
    public List<String> listProperty_distinct(String fieldPath) {
        List<String> list = userDAO
                .select(fieldPath)
                .distinct()
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listNestedPropertyOneLevel_implicitJoin(String fieldPath) {
        return listProperty(fieldPath);
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyOneLevel_explicitInnerJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins(String fieldPath) {
        return listProperty(fieldPath);
    }

    @Override
    public List<String> testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "role")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_implicitJoins_distinct(String fieldPath) {
        return listProperty_distinct(fieldPath);
    }

    @Override
    List<String> listNestedPropertyTwoLevels_explicitLeftJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedPropertyTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .innerJoin("a.state", "s")
                .getResultList();
    }
}
