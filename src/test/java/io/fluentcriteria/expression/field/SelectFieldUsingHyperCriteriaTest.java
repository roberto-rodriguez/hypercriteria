package io.fluentcriteria.expression.field;

import io.fluentcriteria.FluentCriteria;
import io.sample.model.User;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author rrodriguez
 */
class SelectFieldUsingHyperCriteriaTest extends BaseSelectFieldTest {

    @Override
    public Object selectField(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .getSingleResult();
    }

    @Override
    Object selectField_withRootAlias(String fieldPath) {
        return FluentCriteria.using(entityManager)
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
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .getSingleResult();
    }

    @Override
    public Object selectNestedFieldTwoLevels_implicitJoin(String fieldPath) {
        return selectField(fieldPath);
    }

    @Override
    public Object selectNestedFieldTwoLevels_explicitLeftJoin(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getSingleResult();
    }

    @Override
    public List<String> listField(String fieldPath) {
        List<String> list = FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listField_distinct(String fieldPath) {
        List<String> list = FluentCriteria.using(entityManager)
                .select(fieldPath)
                .distinct()
                .from(User.class)
                .getResultList(String.class);

        return list;
    }

    @Override
    public List<String> listNestedFieldOneLevel_implicitJoin(String fieldPath) {
        return listField(fieldPath);
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitLeftJoin(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldOneLevel_explicitInnerJoin(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .innerJoin("address", "a")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins(String fieldPath) {
        return listField(fieldPath);
    }

    @Override
    public List<String> testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "role")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select("address.state.name")
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_implicitJoins_distinct(String fieldPath) {
        return listField_distinct(fieldPath);
    }

    @Override
    List<String> listNestedFieldTwoLevels_explicitLeftJoins(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .leftJoin("a.state", "s")
                .getResultList();
    }

    @Override
    List<String> listNestedFieldTwoLevels_explicitLeftThenInnerJoins(String fieldPath) {
        return FluentCriteria.using(entityManager)
                .select(fieldPath)
                .from(User.class)
                .leftJoin("address", "a")
                .innerJoin("a.state", "s")
                .getResultList();
    }
}
