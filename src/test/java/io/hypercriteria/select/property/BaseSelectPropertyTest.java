package io.hypercriteria.select.property;

import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.Role;
import io.sample.model.State;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rrodriguez
 */
abstract class BaseSelectPropertyTest extends BaseTest {

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
    abstract Object selectProperty(String fieldPath);

    abstract Object selectNestedPropertyOneLevel_implicitJoin(String fieldPath);

    abstract Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath);

    abstract Object selectNestedPropertyTwoLevels_implicitJoin(String fieldPath);

    abstract Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath);

    // List
    abstract List<String> listProperty(String fieldPath);

    abstract List<String> listProperty_distinct(String fieldPath);

    abstract List<String> listNestedPropertyOneLevel_implicitJoin(String fieldPath);

    abstract List<String> testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath(String fieldPath);

    abstract List<String> listNestedPropertyOneLevel_explicitLeftJoin(String fieldPath);

    abstract List<String> listNestedPropertyOneLevel_explicitInnerJoin(String fieldPath);

    abstract List<String> listNestedPropertyTwoLevels_implicitJoins(String fieldPath);

    abstract List<String> listNestedPropertyTwoLevels_implicitJoins_reuseExplicitJoins(String fieldPath);

    abstract List<String> listNestedPropertyTwoLevels_implicitJoins_distinct(String fieldPath);

    abstract List<String> listNestedPropertyTwoLevels_explicitLeftJoins(String fieldPath);

    abstract List<String> listNestedPropertyTwoLevels_explicitLeftThenInnerJoins(String fieldPath);

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

//    @Test
//    void testSelectProperty() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String firstName = (String) selectProperty("firstName");
//
//        assertTrue(List.of("John", "Jane").contains(firstName));
//    }
//
//    @Test
//    void testSelectNestedPropertyOneLevel_implicitJoin() {
//        userDAO.saveOrUpdate(USER_1);
//
//        String street = (String) selectNestedPropertyOneLevel_implicitJoin("address.street");
//
//        assertEquals(USER_1.getAddress().getStreet(), street);
//    }
//
//    @Test
//    void testSelectNestedPropertyOneLevel_explicitLeftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String street = (String) selectNestedPropertyOneLevel_explicitLeftJoin("a.street");
//
//        assertTrue(
//                List.of(
//                        USER_1.getAddress().getStreet(),
//                        USER_2.getAddress().getStreet()
//                ).contains(street)
//        );
//    }
//
//    @Test
//    void testSelectNestedPropertyTwoLevels_implicitJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String stateName = (String) selectNestedPropertyTwoLevels_implicitJoin("address.state.name");
//
//        assertTrue(
//                List.of(
//                        USER_1.getAddress().getState().getName(),
//                        USER_2.getAddress().getState().getName()
//                ).contains(stateName)
//        );
//
//    }
//
//    @Test
//    void testSelectNestedPropertyTwoLevels_explicitLeftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String stateName = (String) selectNestedPropertyTwoLevels_explicitLeftJoin("s.name");
//
//        assertTrue(
//                List.of(
//                        USER_1.getAddress().getState().getName(),
//                        USER_2.getAddress().getState().getName()
//                ).contains(stateName)
//        );
//    }
//
//    @Test
//    void testListProperty() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//        userDAO.saveOrUpdate(USER_2);
//
//        List<String> list = listProperty("firstName");
//
//        assertEquals(3, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals("Jane", list.get(0));
//        assertEquals("Jane", list.get(1));
//        assertEquals("John", list.get(2));
//    }
//
//    @Test
//    void testListProperty_distinct() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//        userDAO.saveOrUpdate(USER_2); //Repeated user, should return same firstName
//
//        List<String> list = listProperty_distinct("firstName");
//
//        assertEquals(2, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals("Jane", list.get(0));
//        assertEquals("John", list.get(1));
//    }
//
//    @Test
//    void testListNestedPropertyOneLevel_implicitJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//
//        List<String> list = listNestedPropertyOneLevel_implicitJoin("address.street");
//
//        assertEquals(2, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(null, list.get(0));
//        assertEquals("123 Main Street", list.get(1));
//    }
//
//    @Test
//    void testListNestedPropertyOneLevel_explicitLeftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//
//        List<String> list = listNestedPropertyOneLevel_explicitLeftJoin("a.street");
//
//        assertEquals(2, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(null, list.get(0));
//        assertEquals("123 Main Street", list.get(1));
//    }
//
//    @Test
//    void testListNestedPropertyOneLevel_explicitInnerJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//
//        List<String> list = listNestedPropertyOneLevel_explicitInnerJoin("a.street");
//
//        assertEquals(1, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals("123 Main Street", list.get(0));
//    }
//
//    @Test
//    void testListNestedPropertyTwoLevels_implicitJoins() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included
//
//        List<String> list = listNestedPropertyTwoLevels_implicitJoins("address.state.name");
//
//        assertEquals(3, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(null, list.get(0));
//        assertEquals(null, list.get(1));
//        assertEquals("Georgia", list.get(2));
//    }

    //Example
    //.select("role.name")  
    //.leftJoin("address", "a")
    //.leftJoin("a.state", "role")
    //The user has role.name, but since there is an alias name role,
    //it takes presedence, so it should return "Geourgia"
    @Test
    void testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath_aliastTakesPrecedence() {
        userDAO.saveOrUpdate(USER_WITH_ROLE);

        List<String> list = testListNestedPropertyTwoLevels_aliasCollissionWithImplicitPath("role.name");//role here refers to the alias for state 

        assertEquals(1, list.size());

        list.sort(Comparator.nullsFirst(String::compareTo));

        assertEquals("Georgia", list.get(0));
    }

    //Should reuse same joins (Need to check in the logs)
//    @Test
//    void testImplicitJoinsReuseExplicitJoins_whenDeclared() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included
//
//        List<String> list = listNestedPropertyTwoLevels_implicitJoins_reuseExplicitJoins("address.state.name");
//
//        assertEquals(3, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(null, list.get(0));
//        assertEquals(null, list.get(1));
//        assertEquals("Georgia", list.get(2));
//    }
//
//    @Test
//    void testListNestedPropertyTwoLevels_implicitJoins_distinct() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        List<String> list = listNestedPropertyTwoLevels_implicitJoins_distinct("address.state.name");
//
//        assertEquals(2, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals("District of Columbia", list.get(0));
//        assertEquals("Georgia", list.get(1));
//    }
//
//    @Test
//    void testListNestedPropertyTwoLevels_explicitLeftJoins() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included
//
//        List<String> list = listNestedPropertyTwoLevels_explicitLeftJoins("s.name");
//
//        assertEquals(3, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(null, list.get(0));
//        assertEquals(null, list.get(1));
//        assertEquals("Georgia", list.get(2));
//    }
//
//    @Test
//    void testListNestedPropertyTwoLevels_explicitLeftThenInnerJoins() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included
//
//        List<String> list = listNestedPropertyTwoLevels_explicitLeftThenInnerJoins("s.name");
//
//        assertEquals(1, list.size());
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals("Georgia", list.get(0));
//    }
}
