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
        this.key = key;
        this.fetch = fetch;
    }

    @Override
    protected From<?, ?> getFrom() {
        return (From<?, ?>) fetch;
    }

    @Override
    public boolean needsLazyInitialization() {
        return false;
    }
}
