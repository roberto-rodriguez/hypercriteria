package io.hypercriteria.context;

import java.util.Optional;
import java.util.function.Function;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rrodriguez
 */
@Setter
@Getter
public class PathMetadata {

    private Optional<String> terminal;
    private Class<?> javaType;
    private boolean isAssociation;
    private Class<?> returnType;
    // Examples:
    // count -> Long
    // avg   -> Double
    // sum   -> numeric promotion type

    public PathMetadata(
            Optional<String> terminal,
            Class<?> javaType,
            boolean isAssociation,
            Function<Class<?>, Class<?>> returnTypeResolver
    ) {
        this.terminal = terminal;
        this.javaType = javaType;
        this.isAssociation = isAssociation;
        this.returnType = returnTypeResolver.apply(javaType);
    }
}
