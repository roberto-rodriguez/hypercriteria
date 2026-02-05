package io.fluentcriteria.context;

import io.fluentcriteria.util.ObjectUtils;
import javax.persistence.criteria.JoinType;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@ToString
public final class JoinKey {

    public final JoinNode parent;
    public final String field;
    public final JoinType joinType;

    public JoinKey(JoinNode parent, String field, JoinType joinType) {
        this.parent = parent;
        this.field = field;
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
                && ObjectUtils.equals(field, that.field)
                && joinType == that.joinType;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(parent, field, joinType);
    }
}
