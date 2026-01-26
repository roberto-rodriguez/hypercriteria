 
package io.hypercriteria.util;

/**
 *
 * @author rrodriguez
 */
public final class ObjectUtils {

    private ObjectUtils() {
        // utility class
    }

    /**
     * Null-safe equality check.
     * Equivalent to java.util.Objects.equals(a, b)
     */
    public static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Null-safe hash code.
     * Equivalent to java.util.Objects.hash(values...)
     */
    public static int hash(Object... values) {
        if (values == null) {
            return 0;
        }

        int result = 1;
        for (Object value : values) {
            result = 31 * result + (value == null ? 0 : value.hashCode());
        }
        return result;
    }
}
