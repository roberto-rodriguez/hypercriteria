package io.hypercriteria;

import io.hypercriteria.base.Selectable;
import io.hypercriteria.criterion.ProjectionList;
import io.hypercriteria.criterion.expression.Abs;
import io.hypercriteria.criterion.expression.Avg;
import io.hypercriteria.criterion.expression.Count;
import io.hypercriteria.criterion.expression.CountDistinct;
import io.hypercriteria.criterion.expression.Dto;
import io.hypercriteria.criterion.expression.Max;
import io.hypercriteria.criterion.expression.Min;
import io.hypercriteria.criterion.expression.base.BaseExpression;
import io.hypercriteria.criterion.expression.Sum;
import io.hypercriteria.criterion.expression.Attribute;
import io.hypercriteria.criterion.predicate.And;
import io.hypercriteria.criterion.predicate.GreaterThan;
import io.hypercriteria.criterion.predicate.Or;
import io.hypercriteria.criterion.predicate.base.BasePredicate;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author rrodriguez
 */
public class FluentCriteria implements Selectable {

    private final EntityManager entityManager;

    private FluentCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static FluentCriteria using(EntityManager entityManager) {
        return new FluentCriteria(entityManager);
    }

//    ------- Selects ------------
    @Override
    public Criteria select() {
        return Criteria.Builder.create(entityManager).build();
    }

    @Override
    public Criteria select(String fieldPath) {
        return select(attribute(fieldPath));
    }

    @Override
    public Criteria select(Class resultType) {
        return select(dto(resultType));
    }

    @Override
    public Criteria select(BaseExpression projection) {
        return Criteria.Builder.create(entityManager)
                .projection(projection)
                .build();
    }

    // -------- Projections ------------
    public static ProjectionList projectionList() {
        return new ProjectionList();
    }

    public static Attribute attribute(String fieldPath) {
        return new Attribute(fieldPath);
    }

    public static Dto dto(Class<?> dtoType) {
        return new Dto(dtoType);
    }

//    public static PropertyProjection groupProperty(String fieldPath) {
//        PropertyProjection propertyProjection = new PropertyProjection(fieldPath);
//        propertyProjection.setGroupBy(true);
//        return propertyProjection;
//    }
    public static Abs abs(String fieldPath) {
        return new Abs(fieldPath);
    }

    public static Abs abs(BaseExpression nestedExpression) {
        return new Abs(nestedExpression);
    }

    public static Avg avg(String fieldPath) {
        return new Avg(fieldPath);
    }

    public static Count count() {
        return new Count();
    }

    public static Count count(String fieldPath) {
        return new Count(fieldPath);
    }

    public static CountDistinct countDistinct() {
        return new CountDistinct();
    }

    public static CountDistinct countDistinct(String fieldPath) {
        return new CountDistinct(fieldPath);
    }

    public static Max max(String fieldPath) {
        return new Max(fieldPath);
    }

    public static Min min(String fieldPath) {
        return new Min(fieldPath);
    }

    public static Sum sum(String fieldPath) {
        return new Sum(fieldPath);
    }

    public static Sum sum(BaseExpression nestedExpression) {
        return new Sum(nestedExpression);
    }

    // ---- Predicates
    public static class Predicates {

        public static And and(BasePredicate p1, BasePredicate p2) {
            return new And(p1, p2);
        }

        public static And and(BasePredicate... predicates) {
            return new And(predicates);
        }

        public static And and(List<BasePredicate> predicates) {
            return new And(predicates);
        }

        public static Or or(BasePredicate p1, BasePredicate p2) {
            return new Or(p1, p2);
        }

        public static Or or(BasePredicate... predicates) {
            return new Or(predicates);
        }

        public static Or or(List<BasePredicate> predicates) {
            return new Or(predicates);
        }

        public static <T extends Comparable<T>> GreaterThan greaterThan(String path, T value) {
            return new GreaterThan<>(path, value);
        }

        public static <T extends Comparable<T>> GreaterThan greaterThan(BaseExpression expression, T value) {
            return new GreaterThan<>(expression, value);
        }

        public static <T extends Comparable<T>> GreaterThan greaterThan(String path, BaseExpression expressionValue) {
            return new GreaterThan<>(path, expressionValue);
        }

        public static <T extends Comparable<T>> GreaterThan greaterThan(BaseExpression expression, BaseExpression expressionValue) {
            return new GreaterThan<>(expression, expressionValue);
        }
    }

}
