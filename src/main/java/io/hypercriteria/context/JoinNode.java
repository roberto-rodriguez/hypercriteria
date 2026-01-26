package io.hypercriteria.context;

import io.hypercriteria.util.ObjectUtils;
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

    public From<?, ?> toFrom(QueryContext ctx) {
        System.out.println("DEBUG:: JoinNode.toFrom");
        System.out.println("DEBUG:: this is joinNode = " + this.toString());

        if (from != null) {
            System.out.println("DEBUG:: JoinNode.toFrom -> from != null");
            return from;
        }

        JoinNode parent = ctx.getRootNode();

        if (key != null && key.parent != null) {
            parent = key.parent;
            System.out.println("DEBUG:: JoinNode.toFrom -> parent = key.parent = " + key.parent);
        } else {
            System.out.println("DEBUG:: JoinNode.toFrom -> parent = ctx.getRootNode()");
        }

        From<?, ?> parentFrom = parent.toFrom(ctx);

        System.out.printf("DEBUG:: JoinNode.toFrom -> this.from = parentFrom.join(%s, %s)", key.attribute, key.joinType);
        System.out.println("");

        this.from = parentFrom.join(key.attribute, key.joinType);

        if (alias != null) {
            System.out.println("DEBUG:: JoinNode.toFrom -> this.from.alias(" + alias + ")");
            this.from.alias(alias);
        }

        return from;
    }

}
