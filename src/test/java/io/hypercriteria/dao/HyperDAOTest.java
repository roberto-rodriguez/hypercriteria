package io.hypercriteria.dao;

import io.sample.dao.UserDAO;
import io.sample.model.User;
import io.utility.BaseTest;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rrodriguez
 */
class HyperDAOTest extends BaseTest {

    private UserDAO userDAO;

    @Override
    protected void beforeEach() {
        userDAO = new UserDAO();
        userDAO.setEntityManager(entityManager);  // assign manually 
    }

    @Test
    void testSaveAndFindById() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Smith")
                .build();

        User saved = userDAO.saveOrUpdate(user);

        User actual = userDAO.findById(saved.getId());

        assertEquals(actual.getFirstName(), user.getFirstName());
        assertEquals(actual.getLastName(), user.getLastName());
    }
}
