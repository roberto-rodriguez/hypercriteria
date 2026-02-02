package io.fluentcriteria.expression.attribute;

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
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectAttributeTest extends BaseTest {

    protected UserDAO userDAO;

    private static final Boolean DISABLE_ALL = false;//Used to test specific test cases

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
    abstract Object selectAttribute(String fieldPath);

    abstract Object selectAttribute_withRootAlias(String fieldPath);

    abstract Object selectNestedAttributeOneLevel_implicitJoin(String fieldPath);

    abstract Object selectNestedAttributeOneLevel_explicitLeftJoin(String fieldPath);

    abstract Object selectNestedAttributeTwoLevels_implicitJoin(String fieldPath);

    abstract Object selectNestedAttributeTwoLevels_explicitLeftJoin(String fieldPath);

    // List
    abstract List<String> listAttribute(String fieldPath);

    abstract List<String> listAttribute_distinct(String fieldPath);

    abstract List<String> listNestedAttributeOneLevel_implicitJoin(String fieldPath);

    abstract List<String> testListNestedAttributeTwoLevels_aliasCollissionWithImplicitPath(String fieldPath);

    abstract List<String> listNestedAttributeOneLevel_explicitLeftJoin(String fieldPath);

    abstract List<String> listNestedAttributeOneLevel_explicitInnerJoin(String fieldPath);

    abstract List<String> listNestedAttributeTwoLevels_implicitJoins(String fieldPath);

    abstract List<String> listNestedAttributeTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath);

    abstract List<String> listNestedAttributeTwoLevels_implicitJoins_distinct(String fieldPath);

    abstract List<String> listNestedAttributeTwoLevels_explicitLeftJoins(String fieldPath);

    abstract List<String> listNestedAttributeTwoLevels_explicitLeftThenInnerJoins(String fieldPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

    @Test
    void testSelectAttribute() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String firstName = (String) selectAttribute("firstName");

        assertTrue(List.of("John", "Jane").contains(firstName));
    }

    @Test
    void testSelectAttribute_withRootAlias() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);

        String firstName = (String) selectAttribute_withRootAlias("u.firstName");

        assertEquals("John", firstName);
    }

    @Test
    void testSelectAttribute_typoInAttributeName() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectAttribute("firstNameX")
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [firstNameX] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );
    }

    @Test
    void testSelectAttribute_emptyFieldPath() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectAttribute("")
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );
    }

    @Test
    void testSelectNestedAttributeOneLevel_implicitJoin() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);

        String street = (String) selectNestedAttributeOneLevel_implicitJoin("address.street");

        assertEquals(USER_1.getAddress().getStreet(), street);
    }

    @Test
    void testSelectNestedAttributeOneLevel_implicitJoin_typoInAssociationName() {
        if (DISABLE_ALL) {
            return;
        }
        userDAO.saveOrUpdate(USER_1);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> selectNestedAttributeOneLevel_implicitJoin("adress.street")//Typo in adress
        );

        assertEquals(
                "Unable to locate Attribute  with the the given name [adress] on this ManagedType [io.sample.model.User]",
                ex.getMessage()
        );
    }

    @Test
    void testSelectNestedAttributeOneLevel_explicitLeftJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String street = (String) selectNestedAttributeOneLevel_explicitLeftJoin("a.street");

        assertTrue(
                List.of(
                        USER_1.getAddress().getStreet(),
                        USER_2.getAddress().getStreet()
                ).contains(street)
        );
    }

    @Test
    void testSelectNestedAttributeTwoLevels_implicitJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String stateName = (String) selectNestedAttributeTwoLevels_implicitJoin("address.state.name");

        assertTrue(
                List.of(
                        USER_1.getAddress().getState().getName(),
                        USER_2.getAddress().getState().getName()
                ).contains(stateName)
        );

    }

    @Test
    void testSelectNestedAttributeTwoLevels_explicitLeftJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        String stateName = (String) selectNestedAttributeTwoLevels_explicitLeftJoin("s.name");

        assertTrue(
                List.of(
                        USER_1.getAddress().getState().getName(),
                        USER_2.getAddress().getState().getName()
                ).contains(stateName)
        );
    }

    @Test
    void testListAttribute() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<String> list = listAttribute("firstName");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Jane", list.get(0));
        assertEquals("Jane", list.get(1));
        assertEquals("John", list.get(2));
    }

    @Test
    void testListAttribute_distinct() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2); //Repeated user, should return same firstName

        List<String> list = listAttribute_distinct("firstName");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Jane", list.get(0));
        assertEquals("John", list.get(1));
    }

    @Test
    void testListNestedAttributeOneLevel_implicitJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedAttributeOneLevel_implicitJoin("address.street");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals("123 Main Street", list.get(1));
    }

    @Test
    void testListNestedAttributeOneLevel_explicitLeftJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedAttributeOneLevel_explicitLeftJoin("a.street");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals("123 Main Street", list.get(1));
    }

    @Test
    void testListNestedAttributeOneLevel_explicitInnerJoin() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);

        List<String> list = listNestedAttributeOneLevel_explicitInnerJoin("a.street");

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("123 Main Street", list.get(0));
    }

    @Test
    void testListNestedAttributeTwoLevels_implicitJoins() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedAttributeTwoLevels_implicitJoins("address.state.name");

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
    void testListNestedAttributeTwoLevels_aliasCollissionWithImplicitPath_aliastTakesPrecedence() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_WITH_ROLE);

        List<String> list = testListNestedAttributeTwoLevels_aliasCollissionWithImplicitPath("role.name");//role here refers to the alias for state 

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Georgia", list.get(0));
    }

    //Should reuse same joins (Need to check in the logs)
    @Test
    void testImplicitJoinsReuseExplicitJoins_whenDeclared() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedAttributeTwoLevels_implicitJoins_reuseExplicitJoins("address.state.name");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals(null, list.get(1));
        assertEquals("Georgia", list.get(2));
    }

    @Test
    void testListNestedAttributeTwoLevels_implicitJoins_distinct() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);

        List<String> list = listNestedAttributeTwoLevels_implicitJoins_distinct("address.state.name");

        assertEquals(2, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("District of Columbia", list.get(0));
        assertEquals("Georgia", list.get(1));
    }

    @Test
    void testListNestedAttributeTwoLevels_explicitLeftJoins() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedAttributeTwoLevels_explicitLeftJoins("s.name");

        assertEquals(3, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals(null, list.get(0));
        assertEquals(null, list.get(1));
        assertEquals("Georgia", list.get(2));
    }

    @Test
    void testListNestedAttributeTwoLevels_explicitLeftThenInnerJoins() {
        if (DISABLE_ALL) {
            return;
        }

        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included

        List<String> list = listNestedAttributeTwoLevels_explicitLeftThenInnerJoins("s.name");

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Georgia", list.get(0));
    }
}
