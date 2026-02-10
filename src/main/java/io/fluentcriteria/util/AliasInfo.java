package io.fluentcriteria.util;

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

    public AliasInfo(String alias, JoinType joinType) { 
        this.alias = alias;
        this.joinType = joinType; 
    }
}
