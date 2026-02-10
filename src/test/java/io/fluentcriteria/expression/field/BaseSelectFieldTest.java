package io.fluentcriteria.expression.field;

import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.Role;
import io.sample.model.State;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Disabled;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectFieldTest extends BaseTest {

    protected UserDAO userDAO;

    private static final User USER_1 = User.builder()
            .firstName("John")
            .lastName("Smith")
            .address(
                    Address.builder()
                            .street("123 Main Street")
                            .city("Atlanta")
                            .state(
                                    State.builder()
                                            .code("GA")
                                            .name("Georgia").build()
                            ).build()
            )
            .build();

    private static final User USER_2 = User.builder()
            .firstName("Jane")
            .lastName("Doe")
            .address(
                    Address.builder()
                            .street("1600 Pensylvania Ave")
                            .city("Whashington")
                            .state(
                                    State.builder()
                                            .code("DC")
                                            .name("District of Columbia").build()
                            ).build()
            )
            .build();

    private static final User USER_WITHOUT_ADDRESS = User.builder()
            .firstName("Noaddress")
            .lastName("User")
            .build();

    private static final User USER_WITHOUT_STATE = User.builder()
            .firstName("Nostate")
            .lastName("User")
            .address(
                    Address.builder()
                            .street("1600 Pensylvania Ave")
                            .city("Whashington")
                            .build()
            )
            .build();

    private static final User USER_WITH_ROLE = USER_1.toBuilder()
            .role(Role.builder().name("Admin").build())
            .build();

    // Select unique result
    abstract Object selectField(String fieldPath);

    abstract Object selectField_withRootAlias(String fieldPath);

    abstract Object selectNestedFieldOneLevel_implicitJoin(String fieldPath);

    abstract Object selectNestedFieldOneLevel_explicitLeftJoin(String fieldPath);

    abstract Object selectNestedFieldTwoLevels_implicitJoin(String fieldPath);

    abstract Object selectNestedFieldTwoLevels_explicitLeftJoin(String fieldPath);

    // List
    abstract List<String> listField(String fieldPath);

    abstract List<String> listField_distinct(String fieldPath);

    abstract List<String> listNestedFieldOneLevel_implicitJoin(String fieldPath);

    abstract List<String> testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath(String fieldPath);

    abstract List<String> listNestedFieldOneLevel_explicitLeftJoin(String fieldPath);

    abstract List<String> listNestedFieldOneLevel_explicitInnerJoin(String fieldPath);

    abstract List<String> listNestedFieldTwoLevels_implicitJoins(String fieldPath);

    abstract List<String> listNestedFieldTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath);

    abstract List<String> listNestedFieldTwoLevels_implicitJoins_distinct(String fieldPath);

    abstract List<String> listNestedFieldTwoLevels_explicitLeftJoins(String fieldPath);

    abstract List<String> listNestedFieldTwoLevels_explicitLeftThenInnerJoins(String fieldPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

    @Test
    void testSelectField() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String firstName = (String) selectField("firstName");

        assertTrue(List.of("John", "Jane").contains(firstName));
    }

    @Test
    void testSelectField_withRootAlias() {
        userDAO.saveOrUpdate(USER_1);

        String firstName = (String) selectField_withRootAlias("u.firstName");

        assertEquals("John", firstName);
    }

    @Test
    void testSelectField_typoInFieldName() {
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectField("firstNameX")
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [firstNameX] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );

    }

    @Test
    void testSelectField_emptyFieldPath() {
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectField("")
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );
    }

    @Test
    void testSelectNestedFieldOneLevel_implicitJoin() {
        userDAO.saveOrUpdate(USER_1);

        String street = (String) selectNestedFieldOneLevel_implicitJoin("address.street");

        assertEquals(USER_1.getAddress().getStreet(), street);
    }

    @Test
    void testSelectNestedFieldOneLevel_implicitJoin_typoInAssociationName() {
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectNestedFieldOneLevel_implicitJoin("adress.street")//Typo in adress
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [adress] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );
    }

    @Test
    void testSelectNestedFieldOneLevel_explicitLeftJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String street = (String) selectNestedFieldOneLevel_explicitLeftJoin("a.street");

        assertTrue(
                List.of(
                        USER_1.getAddress().getStreet(),
                        USER_2.getAddress().getStreet()
                ).contains(street)
        );
    }

    @Test
    void testSelectNestedFieldTwoLevels_implicitJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String stateName = (String) selectNestedFieldTwoLevels_implicitJoin("address.state.name");

        assertTrue(
                List.of(
                        USER_1.getAddress().getState().getName(),
                        USER_2.getAddress().getState().getName()
                ).contains(stateName)
        );

    }

    @Test
    void testSelectNestedFieldTwoLevels_explicitLeftJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String stateName = (String) selectNestedFieldTwoLevels_explicitLeftJoin("s.name");

        assertTrue(
                List.of(
                        USER_1.getAddress().getState().getName(),
                        USER_2.getAddress().getState().getName()
                ).contains(stateName)
        );
    }

    @Test
    void testListField() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<String> list = listField("firstName");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Jane", list.get(0));
        assertEquals("Jane", list.get(1));
        assertEquals("John", list.get(2));
    }

    @Test
    void testListField_distinct() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2); //Repeated user, should return same firstName

        List<String> list = listField_distinct("firstName");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Jane", list.get(0));
        assertEquals("John", list.get(1));
    }

    @Test
    void testListNestedFieldOneLevel_implicitJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedFieldOneLevel_implicitJoin("address.street");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals("123 Main Street", list.get(1));
    }

    @Test
    void testListNestedFieldOneLevel_explicitLeftJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedFieldOneLevel_explicitLeftJoin("a.street");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals("123 Main Street", list.get(1));
    }

    @Test
    void testListNestedFieldOneLevel_explicitInnerJoin() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedFieldOneLevel_explicitInnerJoin("a.street");

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("123 Main Street", list.get(0));
    }

    @Test
    void testListNestedFieldTwoLevels_implicitJoins() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedFieldTwoLevels_implicitJoins("address.state.name");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals(null, list.get(1));
        assertEquals("Georgia", list.get(2));
    }

    //Example
    //.select("role.name")  
    //.leftJoin("address", "a")
    //.leftJoin("a.state", "role")
    //The user has role.name, but since there is an alias name role,
    //it takes presedence, so it should return "Georgia"
    @Test
    void testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath_aliastTakesPrecedence() {
        userDAO.saveOrUpdate(USER_WITH_ROLE);

        List<String> list = testListNestedFieldTwoLevels_aliasCollissionWithImplicitPath("role.name");//role here refers to the alias for state 

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Georgia", list.get(0));
    }

    //Should reuse same joins (Need to check in the logs)
    @Test
    void testImplicitJoinsReuseExplicitJoins_whenDeclared() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedFieldTwoLevels_implicitJoins_reuseExplicitJoins("address.state.name");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals(null, list.get(1));
        assertEquals("Georgia", list.get(2));
    }

    @Test
    void testListNestedFieldTwoLevels_implicitJoins_distinct() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        List<String> list = listNestedFieldTwoLevels_implicitJoins_distinct("address.state.name");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("District of Columbia", list.get(0));
        assertEquals("Georgia", list.get(1));
    }

    @Test
    void testListNestedFieldTwoLevels_explicitLeftJoins() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedFieldTwoLevels_explicitLeftJoins("s.name");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals(null, list.get(1));
        assertEquals("Georgia", list.get(2));
    }

    @Test
    void testListNestedFieldTwoLevels_explicitLeftThenInnerJoins() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedFieldTwoLevels_explicitLeftThenInnerJoins("s.name");

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Georgia", list.get(0));
    }
}
