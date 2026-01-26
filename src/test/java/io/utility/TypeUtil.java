package io.utility;

import io.sample.model.User;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 *
 * @author rrodriguez
 */
public final class TypeUtil {

    private static final Map<String, Class<?>> TYPES = Map.of(
            "intValue", Integer.class,
            "longValue", Long.class,
            "floatValue", Float.class,
            "doubleValue", Double.class,
            "bigInteger", BigInteger.class,
            "bigDecimal", BigDecimal.class,
            "user", User.class
    );

    private TypeUtil() {
    }

    public static Class<?> getType(String fieldPath) {
        if (fieldPath == null || fieldPath.isBlank()) {
            throw new IllegalArgumentException("fieldPath must not be null or blank");
        }

        // Extract last segment (supports nested paths like "payment.amount")
        int lastDot = fieldPath.lastIndexOf('.');
        String fieldName = (lastDot >= 0)
                ? fieldPath.substring(lastDot + 1)
                : fieldPath;

        Class<?> type = TYPES.get(fieldName);
        if (type == null) {
            throw new IllegalArgumentException(
                    "Unknown field path: " + fieldPath
            );
        }

        return type;
    }
}
