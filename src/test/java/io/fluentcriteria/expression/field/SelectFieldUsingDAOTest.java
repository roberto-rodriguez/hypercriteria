package io.fluentcriteria.expression.field;

import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectFieldUsingDAOTest extends BaseSelectFieldTest {

    @Override
    public Object selectField(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getSingleResult();
    }

    @Override
    Object selectField_withRootAlias(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .from(User.class, "u")
                .getSingleResult();
    }

    @Override
    public Object selectNestedFieldOneLevel_implicitJoin(String fieldPath) {
        return selectField(fieldPath);
    }

    @Override
    public Object selectNestedFieldOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getSingleResult();
    }

    @Override
    public Object selectNestedFieldTwoLevels_implicitJoin(String fieldPath) {
        return selectField(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedFieldTwoLevels_explicitLeftJoins(fieldPath);
    }

    @Override
    public Object selectNestedFieldTwoLevels_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getSingleResult();
    }

    @Override
    public List<String> listField(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getResultList();
    }

    @Override
    public List<String> listField_distinct(String fieldPath) {
        List<String> list = userDAO
                .select(fieldPath)
                .distinct()
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listNestedFieldOneLevel_implicitJoin(String fieldPath) {
        return listField(fieldPath);
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitInnerJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins(String fieldPath) {
        return listField(fieldPath);
    }

    @Override
    public List<String> testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "role")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_distinct(String fieldPath) {
        return listField_distinct(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_explicitLeftJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .innerJoin("a.state", "s")
                .getResultList();
    }
}
