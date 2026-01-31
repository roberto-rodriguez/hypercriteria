package io.hypercriteria.criterion.predicate.base;

import io.hypercriteria.context.PathExpression;
import java.util.function.Function;

/**
 *
 * @author rrodriguez
 */
public abstract class ComparablePredicate<T extends Comparable<T>> extends BasePredicate {

    private final T value;

    public ComparablePredicate(String fieldPath, T value) {
//        this.pathExpression = new PathExpression(fieldPath);
        this.value = value;

    }
}
