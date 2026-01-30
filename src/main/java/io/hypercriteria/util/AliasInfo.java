package io.hypercriteria.util;

import javax.persistence.criteria.JoinType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@Getter
@Setter
@ToString
public class AliasInfo {

    private final String alias;
    private final JoinType joinType;
//    private final Class<?> javaType;

    public AliasInfo(String alias, JoinType joinType) {//, Class<?> javaType
        this.alias = alias;
        this.joinType = joinType;
//        this.javaType = javaType;
    }
}
