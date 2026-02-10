package io.fluentcriteria.context;

import io.fluentcriteria.util.ObjectUtils;
import javax.persistence.criteria.JoinType;
import lombok.ToString;

@ToString
public final class JoinKey {

    public final JoinNode parent;
    public final String field;
    public final JoinType joinType;

    // NEW: distinguishes explicit joins with different aliases
    public final String explicitAlias;

    public JoinKey(JoinNode parent, String field, JoinType joinType, String explicitAlias) {
        this.parent = parent;
        this.field = field;
        this.joinType = joinType;
        this.explicitAlias = explicitAlias;
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
                && joinType == that.joinType
                && ObjectUtils.equals(explicitAlias, that.explicitAlias);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(parent, field, joinType, explicitAlias);
    }
}
