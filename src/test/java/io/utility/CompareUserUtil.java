package io.utility;

import io.sample.model.Address;
import io.sample.model.Role;
import io.sample.model.State;
import io.sample.model.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *
 * @author rrodriguez
 */
public class CompareUserUtil {

    public static void assertUserEqualsWithDependencies(User expected, User actual) {
        assertUserEquals(expected, actual);
        // Address
        assertAddressExistAndEquals(expected.getAddress(), actual.getAddress());

        // Role
        assertRoleEquals(expected.getRole(), actual.getRole());
    }

    public static void assertUserEqualsWithAddress(User expected, User actual) {
        assertUserEquals(expected, actual);
        // Address
        assertAddressExistAndEquals(expected.getAddress(), actual.getAddress()); 
    }

    public static void assertUserEquals(User expected, User actual) {
        assertNotNull(expected, "Expected user must not be null");
        assertNotNull(actual, "Actual user must not be null");

        // User fields
        assertEquals(expected.getFirstName(), actual.getFirstName(), "firstName");
        assertEquals(expected.getLastName(), actual.getLastName(), "lastName");
        assertEquals(expected.getActive(), actual.getActive(), "active");
        assertEquals(expected.getCreationDate(), actual.getCreationDate(), "creationDate");

    }

    private static void assertAddressExistAndEquals(Address expected, Address actual) {
        if ( actual == null) {
            throw new IllegalArgumentException("Expected addressdres but got null");
        }

        assertEquals(expected.getStreet(), actual.getStreet(), "street");
        assertEquals(expected.getCity(), actual.getCity(), "city");

        // State
        assertStateEquals(expected.getState(), actual.getState());
    }

    private static void assertStateEquals(State expected, State actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual, "state");
            return;
        }

        assertEquals(expected.getCode(), actual.getCode(), "state.code");
        assertEquals(expected.getName(), actual.getName(), "state.name");
    }

    private static void assertRoleEquals(Role expected, Role actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual, "role");
            return;
        }

        assertEquals(expected.getName(), actual.getName(), "role.name");
    }
}
