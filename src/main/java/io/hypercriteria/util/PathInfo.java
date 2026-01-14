package io.hypercriteria.util;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rrodriguez
 */
@Builder
@Setter
@Getter
public class PathInfo {

    private Class<?> javaType;
    private final boolean endsInAssociation;

    private final String lastJoin; // Empty String if there is not join, to indicate root.
    private final Optional<String> attributeName;

    public String getAlias() {
        return attributeName.orElse(lastJoin);
    }

    //If the last segment on the path is an association
    public boolean endsInAssociation() {
        return endsInAssociation;
    }
}
