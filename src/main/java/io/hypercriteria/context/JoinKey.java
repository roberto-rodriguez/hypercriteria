package io.hypercriteria.context;

import javax.persistence.criteria.JoinType;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@ToString
public final class JoinKey {

    public final JoinNode parent;
    public final String attribute;
    public final JoinType joinType;

    public JoinKey(JoinNode parent, String attribute, JoinType joinType) {
        this.parent = parent;
        this.attribute = attribute;
        this.joinType = joinType;
    }

    // equals + hashCode REQUIRED
}
