package io.fluentcriteria.context;

import io.fluentcriteria.util.ObjectUtils;
import javax.persistence.criteria.From;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author rrodriguez
 */
@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
public class JoinNode {

    public final JoinKey key;
    public String alias;          // assigned later
    public From<?, ?> from;       // JPA object (assigned later)

    public JoinNode(JoinKey key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JoinNode || o instanceof FetchNode)) {
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
        System.out.println("DEBUG:: JoinNode.toFrom");
        System.out.println("DEBUG:: this is joinNode = " + this.toString());

        From<?, ?> _from = getFrom();
        if (_from != null) {
            //FetchNodes will return Fetch cased to Join right the way.
            System.out.println("DEBUG:: JoinNode.toFrom -> from != null");
            return _from;
        }

        JoinNode parent = ctx.getRootNode();

        if (key != null && key.parent != null) {
            parent = key.parent;
            System.out.println("DEBUG:: JoinNode.toFrom -> parent = key.parent = " + key.parent);
        } else {
            System.out.println("DEBUG:: JoinNode.toFrom -> parent = ctx.getRootNode()");
        }

        From<?, ?> parentFrom = parent.toFrom(ctx);

        System.out.printf("DEBUG:: JoinNode.toFrom -> this.from = parentFrom.join(%s, %s)", key.field, key.joinType);
        System.out.println("");

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
            System.out.println("DEBUG:: JoinNode.toFrom -> this.from.alias(" + alias + ")");
            this.from.alias(alias);
        }

        return from;
    }

}
