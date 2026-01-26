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

    private String alias;
    private JoinType joinType;
    private Class javaType;

    public AliasInfo(String alias, JoinType joinType, Class javaType) {
        this.alias = alias;
        this.joinType = joinType;
    }

}
