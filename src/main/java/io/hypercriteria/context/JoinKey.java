package io.hypercriteria.context;

import io.hypercriteria.util.ObjectUtils;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinKey)) {
            return false;
        }
        JoinKey that = (JoinKey) o;
        return ObjectUtils.equals(parent, that.parent)
                && ObjectUtils.equals(attribute, that.attribute)
                && joinType == that.joinType;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(parent, attribute, joinType);
    }
}
