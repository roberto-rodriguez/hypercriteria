package io.fluentcriteria.context;

import javax.persistence.criteria.JoinType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class JoinSpec {

    private final String path;        // e.g. "parent" or "u.address"
    private final String alias;       // e.g. "p" (nullable for fetch)
    private final JoinType joinType;
    private final boolean fetch;
    private final Class<?> javaType;  // resolved at registration time

    public JoinSpec(String path, String alias, JoinType joinType, boolean fetch, Class<?> javaType) {
        this.path = path;
        this.alias = alias;
        this.joinType = joinType;
        this.fetch = fetch;
        this.javaType = javaType;
    }
}
