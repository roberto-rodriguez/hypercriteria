package io.hypercriteria.util;

import static org.junit.jupiter.api.Assertions.*;
import io.sample.model.Payment;
import io.sample.model.User;
import io.sample.dao.UserDAO;
import io.sample.model.Address;
import io.sample.model.State;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


class PathUtilTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private UserDAO userDAO;

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
                                            .name("Georgia")
                                            .build()
                            )
                            .build()
            )
            .build();

    private static final User USER_WITH_PAYMENTS = USER_1.toBuilder()
            .payments(new ArrayList<>())
            .build();

    static {
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(1D).build());
        USER_WITH_PAYMENTS.addPayment(Payment.builder().amount(2D).build());
    }

    @BeforeEach
    void beforeEach() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();

        userDAO = new UserDAO();
        userDAO.setEntityManager(em);

        em.getTransaction().begin();
    }

    @AfterEach
    void afterEach() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
        emf.close();
    }

    /* -------------------------------------------------
     * Single-segment paths
     * ------------------------------------------------- */

    @Test
    void singleSegment_attribute() {
        userDAO.saveOrUpdate(USER_1);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "firstName",
                joins
        );

        assertEquals(String.class, info.getJavaType());
        assertFalse(info.isEndsInAssociation());
        assertEquals("firstName", info.getAttributeName().orElseThrow());
        assertEquals("", info.getLastJoin());
        assertTrue(joins.isEmpty());
    }

    @Test
    void singleSegment_association() {
        userDAO.saveOrUpdate(USER_1);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "address",
                joins
        );

        assertEquals(Address.class, info.getJavaType());
        assertTrue(info.isEndsInAssociation());
        assertTrue(info.getAttributeName().isEmpty());
        assertEquals("address", info.getLastJoin());

        assertEquals(1, joins.size());
        assertEquals(JoinType.LEFT, joins.get("address").getJoinType());
    }

    /* -------------------------------------------------
     * Multi-segment paths ending in attribute
     * ------------------------------------------------- */

    @Test
    void nestedAttributePath() {
        userDAO.saveOrUpdate(USER_1);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "address.city",
                joins
        );

        assertEquals(String.class, info.getJavaType());
        assertFalse(info.isEndsInAssociation());
        assertEquals("city", info.getAttributeName().orElseThrow());
        assertEquals("address", info.getLastJoin());

        assertEquals(1, joins.size());
        assertTrue(joins.containsKey("address.city") == false);
        assertTrue(joins.containsKey("address"));
    }

    @Test
    void deepNestedAttributePath() {
        userDAO.saveOrUpdate(USER_1);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "address.state.code",
                joins
        );

        assertEquals(String.class, info.getJavaType());
        assertFalse(info.isEndsInAssociation());
        assertEquals("code", info.getAttributeName().orElseThrow());
        assertEquals("state", info.getLastJoin());

        assertEquals(2, joins.size());
        assertEquals(JoinType.LEFT, joins.get("address").getJoinType());
        assertEquals(JoinType.LEFT, joins.get("address.state").getJoinType());
    }

    /* -------------------------------------------------
     * Paths ending in association
     * ------------------------------------------------- */

    @Test
    void pathEndsInCollectionAssociation() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "payments",
                joins
        );

        assertEquals(List.class, info.getJavaType());
        assertTrue(info.isEndsInAssociation());
        assertTrue(info.getAttributeName().isEmpty());
        assertEquals("payments", info.getLastJoin());

        assertEquals(1, joins.size());
        assertEquals(JoinType.LEFT, joins.get("payments").getJoinType());
    }

    @Test
    void nestedPathEndsInAssociation() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathInfo info = PathUtil.getPathInfo(
                em,
                User.class,
                "payments.user",
                joins
        );

        assertEquals(User.class, info.getJavaType());
        assertTrue(info.isEndsInAssociation());
        assertTrue(info.getAttributeName().isEmpty());
        assertEquals("user", info.getLastJoin());

        assertEquals(2, joins.size());
        assertEquals(JoinType.LEFT, joins.get("payments").getJoinType());
        assertEquals(JoinType.LEFT, joins.get("payments.user").getJoinType());
    }

    /* -------------------------------------------------
     * Join delimiter handling & precedence
     * ------------------------------------------------- */

    @Test
    void joinDelimiter_precedence_inner_over_left() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathUtil.getPathInfo(em, User.class, "payments.amount", joins);
        PathUtil.getPathInfo(em, User.class, "<>payments.amount", joins);

        assertEquals(JoinType.INNER, joins.get("payments").getJoinType());
    }

    @Test
    void joinDelimiter_rightJoin() {
        userDAO.saveOrUpdate(USER_WITH_PAYMENTS);

        LinkedHashMap<String, AliasJoinType> joins = new LinkedHashMap<>();

        PathUtil.getPathInfo(em, User.class, ">payments.amount", joins);

        assertEquals(JoinType.RIGHT, joins.get("payments").getJoinType());
    }

    /* -------------------------------------------------
     * Error handling
     * ------------------------------------------------- */

    @Test
    void invalidAttribute_throwsException() {
        userDAO.saveOrUpdate(USER_1);

        assertThrows(IllegalArgumentException.class, () ->
                PathUtil.getPathInfo(
                        em,
                        User.class,
                        "address.invalidField",
                        new LinkedHashMap<>()
                )
        );
    }
}
