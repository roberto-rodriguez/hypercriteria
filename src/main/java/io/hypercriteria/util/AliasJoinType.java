package io.hypercriteria.util;

import javax.persistence.criteria.JoinType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@Getter
@Setter
@Builder
@ToString
public class AliasJoinType {

    private String alias;
    private JoinType joinType;
}
