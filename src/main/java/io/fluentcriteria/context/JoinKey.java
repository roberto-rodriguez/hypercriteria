package io.fluentcriteria.context;

import io.fluentcriteria.util.ObjectUtils;
import javax.persistence.criteria.JoinType;
import lombok.ToString;

@ToString
public final class JoinKey { 
    public final JoinNode parent;
    public final String field;
    public final JoinType joinType;
    public final String explicitAlias;

    // NEW
    public final boolean hasOnPredicate;

    public JoinKey(JoinNode parent, String field, JoinType joinType, String explicitAlias, boolean hasOnPredicate) {
        this.parent = parent;
        this.field = field;
        this.joinType = joinType;
        this.explicitAlias = explicitAlias;
        this.hasOnPredicate = hasOnPredicate;
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
                && ObjectUtils.equals(explicitAlias, that.explicitAlias)
                && hasOnPredicate == that.hasOnPredicate;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(parent, field, joinType, explicitAlias, hasOnPredicate);
    }
}
