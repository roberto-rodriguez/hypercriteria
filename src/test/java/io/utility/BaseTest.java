package io.utility;
 
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author rrodriguez
 */
public abstract class BaseTest {

    protected static EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    @BeforeAll
    static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("testPU");
    }

    @AfterAll
    static void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();

        beforeEach();

        // Start transaction
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        if (entityManager != null) {
            entityManager.close();
        }
    }

    protected abstract void beforeEach();
}
