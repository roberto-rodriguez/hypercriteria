package io.fluentcriteria.context;

import io.fluentcriteria.FluentCriteria;
import io.fluentcriteria.predicate.And;
import io.fluentcriteria.predicate.base.BasePredicate;
import java.util.Optional;
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
    private Optional<BasePredicate> onPredicate = Optional.empty();

    public JoinSpec(String path, String alias, JoinType joinType, boolean fetch, Class<?> javaType) {
        this.path = path;
        this.alias = alias;
        this.joinType = joinType;
        this.fetch = fetch;
        this.javaType = javaType;
    }

    public void setOnPredicate(BasePredicate predicate) {
        // Root spec guard (from() registers "" joinPath)
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("On clause must be applied after an explicit join, not after from()");
        }

        // Fetch join guard
        if (fetch) {
            throw new IllegalArgumentException("On clause is not supported for fetch joins");
        }

        // Merge if called multiple times (AND)
        if (onPredicate.isPresent()) {
            if (onPredicate.get() instanceof And andPredicate) {
                andPredicate.add(predicate);
            } else {
                And and = FluentCriteria.and(this.onPredicate.get(), predicate);
                this.onPredicate = Optional.of(and);
            }
        } else {
            this.onPredicate = Optional.of(predicate);
        } 
    }
}
