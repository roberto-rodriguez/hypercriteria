package io.fluentcriteria.context;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;

/**
 *
 * @author rrodriguez
 */
public class FetchNode extends JoinNode {

    public Fetch<?, ?> fetch;

    public FetchNode(JoinKey key, Fetch<?, ?> fetch) {
        super(key);
    }

    @Override
    protected From<?, ?> getFrom() {
        return (From<?, ?>) fetch;
    }
}
