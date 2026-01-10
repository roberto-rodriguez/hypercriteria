package io.hypercriteria.util;
 
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.JoinType;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectionBuilderTest {

    @Test
    void nullPath_returnsNull_andDoesNotPopulateMap() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType(null, map);

        assertNull(result);
        assertTrue(map.isEmpty());
    }

    @Test
    void emptyPath_returnsEmpty_andDoesNotPopulateMap() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("", map);

        assertEquals("", result);
        assertTrue(map.isEmpty());
    }

    @Test
    void singleSegment_returnsAsIs_andDoesNotPopulateMap() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("a", map);

        assertEquals("a", result);
        assertTrue(map.isEmpty());
    }

    @Test
    void twoSegments_defaultLeftJoin_onlyRootIsAdded() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("a.b", map);

        assertEquals("a.b", result);
        assertEquals(1, map.size());

        assertAlias(map, "a", "a", JoinType.LEFT);
    }

    @Test
    void twoSegments_rootRightJoin_onlyRootIsAdded() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType(">a.b", map);

        assertEquals("a.b", result);
        assertEquals(1, map.size());

        assertAlias(map, "a", "a", JoinType.RIGHT);
    }

    @Test
    void twoSegments_rootInnerJoin_onlyRootIsAdded() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("<>a.b", map);

        assertEquals("a.b", result);
        assertEquals(1, map.size());

        assertAlias(map, "a", "a", JoinType.INNER);
    }

    @Test
    void threeSegments_allLeftJoins() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("a.b.c", map);

        assertEquals("b.c", result);
        assertEquals(2, map.size());

        assertAlias(map, "a", "a", JoinType.LEFT);
        assertAlias(map, "a.b", "b", JoinType.LEFT);
    }

    @Test
    void fourSegments_mixedJoins() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("a>b<>c.d", map);

        assertEquals("c.d", result);

        assertAlias(map, "a", "a", JoinType.LEFT);
        assertAlias(map, "a.b", "b", JoinType.RIGHT);
        assertAlias(map, "b.c", "c", JoinType.INNER);
    }

    @Test
    void explicitRootInner_thenRightChild() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        String result = ProjectionBuilder.extractAliasAndJoinType("<>a>b.c", map);

        assertEquals("b.c", result);

        assertAlias(map, "a", "a", JoinType.INNER);
        assertAlias(map, "a.b", "b", JoinType.RIGHT);
    }

    @Test
    void joinPrecedence_upgradesLeftToRight() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        ProjectionBuilder.extractAliasAndJoinType("a.b.c", map);
        ProjectionBuilder.extractAliasAndJoinType("a>b.c", map);

        assertAlias(map, "a.b", "b", JoinType.RIGHT);
    }

    @Test
    void joinPrecedence_upgradesRightToInner() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        ProjectionBuilder.extractAliasAndJoinType("a>b.c", map);
        ProjectionBuilder.extractAliasAndJoinType("a<>b.c", map);

        assertAlias(map, "a.b", "b", JoinType.INNER);
    }

    @Test
    void joinPrecedence_doesNotDowngradeInner() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        ProjectionBuilder.extractAliasAndJoinType("a<>b.c", map);
        ProjectionBuilder.extractAliasAndJoinType("a.b.c", map);

        assertAlias(map, "a.b", "b", JoinType.INNER);
    }

    @Test
    void insertionOrder_isPreserved() {
        LinkedHashMap<String, AliasJoinType> map = new LinkedHashMap<>();

        ProjectionBuilder.extractAliasAndJoinType("a>b<>c.d", map);

        String[] expected = { "a", "a.b", "b.c" };
        int i = 0;

        for (String key : map.keySet()) {
            assertEquals(expected[i++], key);
        }
    }

    private static void assertAlias(
            Map<String, AliasJoinType> map,
            String key,
            String expectedAlias,
            JoinType expectedJoinType) {

        assertTrue(map.containsKey(key), "Missing key: " + key);

        AliasJoinType value = map.get(key);

        assertEquals(expectedAlias, value.getAlias(), "Alias mismatch for key: " + key);
        assertEquals(expectedJoinType, value.getJoinType(), "JoinType mismatch for key: " + key);
    }
}
