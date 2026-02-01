package io.hypercriteria.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rrodriguez
 */
public enum NumericType {
    BYTE(Byte.class, Long.class),
    SHORT(Short.class, Long.class),
    INTEGER(Integer.class, Long.class),
    LONG(Long.class, Long.class),
    FLOAT(Float.class, Double.class),
    DOUBLE(Double.class, Double.class),
    BIG_INTEGER(BigInteger.class, BigInteger.class),
    BIG_DECIMAL(BigDecimal.class, BigDecimal.class);

    final Class<?> type;
    final Class<?> promotionTypeWhenSuming;

    NumericType(Class<?> type, Class<?> promotionTypeWhenSuming) {
        this.type = type;
        this.promotionTypeWhenSuming = promotionTypeWhenSuming;
    }

    public static NumericType from(Class<?> clazz) {
        return Arrays.stream(values())
                .filter(n -> n.type.equals(clazz))
                .findFirst()
                .orElseThrow(()
                        -> new IllegalArgumentException("Unsupported type: " + clazz));
    }

    public Class<?> getPromotionTypeWhenSuming() {
        return promotionTypeWhenSuming;
    }

}
