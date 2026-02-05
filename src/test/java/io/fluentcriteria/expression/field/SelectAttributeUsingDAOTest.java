package io.fluentcriteria.expression.field;

import io.sample.model.User;
import java.util.List;

/**
 *
 * @author rrodriguez
 */
class SelectAttributeUsingDAOTest extends BaseSelectAttributeTest {

    @Override
    public Object selectAttribute(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getSingleResult();
    }

    @Override
    Object selectAttribute_withRootAlias(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .from(User.class, "u")
                .getSingleResult();
    }

    @Override
    public Object selectNestedAttributeOneLevel_implicitJoin(String fieldPath) {
        return selectAttribute(fieldPath);
    }

    @Override
    public Object selectNestedAttributeOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getSingleResult();
    }

    @Override
    public Object selectNestedAttributeTwoLevels_implicitJoin(String fieldPath) {
        return selectAttribute(fieldPath);
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return listNestedAttributeTwoLevels_explicitLeftJoins(fieldPath);
    }

    @Override
    public Object selectNestedAttributeTwoLevels_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getSingleResult();
    }

    @Override
    public List<String> listAttribute(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .getResultList();
    }

    @Override
    public List<String> listAttribute_distinct(String fieldPath) {
        List<String> list = userDAO
                .select(fieldPath)
                .distinct()
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listNestedAttributeOneLevel_implicitJoin(String fieldPath) {
        return listAttribute(fieldPath);
    }

    @Override
    List<String> listNestedAttributeOneLevel_explicitLeftJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedAttributeOneLevel_explicitInnerJoin(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins(String fieldPath) {
        return listAttribute(fieldPath);
    }

    @Override
    public List<String> testListNestedAttributeTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "role")
                .getResultList();
    }

    @Override
    List<String> listNestedAttributeTwoLevels_implicitJoins_distinct(String fieldPath) {
        return listAttribute_distinct(fieldPath);
    }

    @Override
    List<String> listNestedAttributeTwoLevels_explicitLeftJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedAttributeTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        return userDAO
                .select(fieldPath)
                .leftJoin("address", "a")
                .innerJoin("a.state", "s")
                .getResultList();
    }
}
