package io.hypercriteria.util;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.JoinType;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProjectionBuilderTest {

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
