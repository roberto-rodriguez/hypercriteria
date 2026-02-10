package io.fluentcriteria.context;

import io.fluentcriteria.util.ObjectUtils;
import javax.persistence.criteria.From;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class JoinNode {

    public JoinKey key;
    public String alias;          // assigned later
    public From<?, ?> from;       // JPA object (assigned later)
    public boolean declaredExplicitly;

    public JoinNode(JoinKey key, boolean declaredExplicitly) {
        this.key = key;
        this.declaredExplicitly = declaredExplicitly;
    }

    //Joins that were declared explicitly but were never used
    public boolean needsLazyInitialization() {
        return declaredExplicitly && from == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinNode)) {
            return false;
        }
        JoinNode that = (JoinNode) o;
        return ObjectUtils.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hash(key);
    }

    protected From<?, ?> getFrom() {
        return from;
    }

    public From<?, ?> toFrom(QueryContext ctx) {
        From<?, ?> _from = getFrom();
        if (_from != null) {
            //FetchNodes will return Fetch cased to Join right the way. 
            return _from;
        }

        JoinNode parent = ctx.getRootNode();

        if (key != null && key.parent != null) {
            parent = key.parent;
        }

        From<?, ?> parentFrom = parent.toFrom(ctx);

        if (key.field == null || key.field.isEmpty()) {
            this.from = parentFrom;//This happens when alias is specified in the 'from()' clause. Example: .from(User.class, "u")
        } else {
            if (key.joinType == null) {
                this.from = parentFrom.join(key.field);
            } else {
                this.from = parentFrom.join(key.field, key.joinType);
            }

        }

        if (alias != null) {
            this.from.alias(alias);
        }

        return from;
    }

}
