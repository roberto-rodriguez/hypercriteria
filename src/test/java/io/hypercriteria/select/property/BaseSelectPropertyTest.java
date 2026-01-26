package io.hypercriteria.select.property;

import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.State;
import io.sample.model.User;
import io.utility.BaseTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    // Select unique result
    abstract Object selectProperty(String fieldPath);

    abstract Object selectNestedPropertyOneLevel_inplicitJoin(String fieldPath);

    abstract Object selectNestedPropertyOneLevel_explicitLeftJoin(String fieldPath);

    abstract Object selectNestedPropertyTwoLevels_inplicitJoin(String fieldPath);

    abstract Object selectNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath);

    // List
    abstract List<String> listProperty(String fieldPath);

//    abstract Object listProperty_distinct(String fieldPath);
//
//    abstract Object listNestedPropertyOneLevel_inplicitJoin(String fieldPath);
//
//    abstract Object listNestedPropertyOneLevel_explicitLeftJoin(String fieldPath);
//
//    abstract Object listNestedPropertyTwoLevels_inplicitJoin(String fieldPath);
//
//    abstract Object listNestedPropertyTwoLevels_explicitLeftJoin(String fieldPath);
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
//        assertEquals(firstName, USER_1.getFirstName());
//    }
//
//    @Test
//    void testSelectNestedPropertyOneLevel_inplicitJoin() {
//        userDAO.saveOrUpdate(USER_1);
//
//        String street = (String) selectNestedPropertyOneLevel_inplicitJoin("address.street");
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
//        assertEquals(USER_1.getAddress().getStreet(), street);
//    }
//
//    @Test
//    void testSelectNestedPropertyTwoLevels_inplicitJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String stateName = (String) selectNestedPropertyTwoLevels_inplicitJoin("address.state.name");
//
//        assertEquals(USER_1.getAddress().getState().getName(), stateName);
//    }
//
//    @Test
//    void testSelectNestedPropertyTwoLevels_explicitLeftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//
//        String stateName = (String) selectNestedPropertyTwoLevels_explicitLeftJoin("s.name");
//
//        assertEquals(USER_1.getAddress().getState().getName(), stateName);
//    }
    @Test
    void testListProperty() {
        userDAO.saveOrUpdate(USER_1);
        userDAO.saveOrUpdate(USER_2);
        userDAO.saveOrUpdate(USER_2);

        List<String> list = listProperty("firstName");

        assertEquals(3, list.size());

        assertEquals("John", list.get(0));
        assertEquals("Jane", list.get(1));
        assertEquals("Jane", list.get(2));
    }

//    @Test
//    void testListProperty_distinct() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_2);
//        userDAO.saveOrUpdate(USER_2); //Repeated user, should return same firstName
//
//        List<String> list = listDistinctByProperty("firstName");
//
//        assertEquals(2, list.size());
//
//        assertEquals("Jane", list.get(0));
//        assertEquals("John", list.get(1));
//    }
//
//    @Test
//    void testSelectProperty_listNestedProperty_leftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//
//        List<String> list = listByProperty("address.street");
//
//        assertEquals(2, list.size());
//
//        assertEquals(null, list.get(0));
//        assertEquals("123 Main Street", list.get(1));
//    }
//
//    @Test
//    void testSelectProperty_listDistinctNestedProperty_leftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//
//        List<String> list = listDistinctByProperty("address.street");
//
//        assertEquals(2, list.size());
//
//        assertEquals(null, list.get(0));
//        assertEquals("123 Main Street", list.get(1));
//    }
//
//    @Test
//    void testSelectProperty_listNestedProperty_innerJoin_aliasBasedFieldPath() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);
//
//        List<String> list = listByPropertyWithInnerJoin("a.street");
//
//        assertEquals(2, list.size());
//
//        assertEquals("123 Main Street", list.get(0));
//    }
//
//    @Test
//    void testSelectProperty_listNestedProperty_innerJoin_withRootAlias_aliasBasedFieldPath() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);
//
//        List<String> list = listByPropertyWithInnerJoin_withRootAlias("a.street");
//
//        assertEquals(2, list.size());
//
//        assertEquals("123 Main Street", list.get(0));
//    }
//
//    @Test
//    void testSelectProperty_listNestedPropertyTwoLevels_leftJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);
//
//        List<String> list = listByProperty("address.state.name");
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(3, list.size());
//
//        assertEquals(null, list.get(0));
//        assertEquals(null, list.get(1));
//        assertEquals("Georgia", list.get(2));
//    }
//    @Test
//    void testSelectProperty_listNestedPropertyTwoLevels_innerJoin() {
//        userDAO.saveOrUpdate(USER_1);
//        userDAO.saveOrUpdate(USER_WITHOUT_ADDRESS);
//        userDAO.saveOrUpdate(USER_WITHOUT_STATE);//Will not be included
//
//        List<String> list = listByProperty("address<>state.name"); //address RIGHT JOIN state
//
//        list.sort(Comparator.nullsFirst(String::compareTo));
//
//        assertEquals(1, list.size());
//
//        assertEquals("Georgia", list.get(0));
//    }
}
