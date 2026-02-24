/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.fluentcriteria.predicate;

import io.fluentcriteria.context.QueryContext;
import io.fluentcriteria.expression.base.BaseExpression;
import io.fluentcriteria.predicate.base.ExpressionValue;
import io.fluentcriteria.predicate.base.PredicateWithValue;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author rrodriguez
 */
public class Like extends PredicateWithValue<String> {
    
    private MatchMode matchMode;
    
    public enum MatchMode {
        EXACT, START, END, ANYWHERE;
        
        public String toMatchString(Object string) {
            return switch (this) {
                case START ->
                    string + "%";
                case END ->
                    "%" + string;
                case ANYWHERE ->
                    "%" + string + "%";
                default ->
                    String.valueOf(string);
            }; //EXACT
        }
    }
    
    protected ExpressionValue<Character> escapeCharExpressionValue;
    
    public Like(BaseExpression expression, String value) {
        super(expression, value);
    }
    
    public Like(BaseExpression expression, String value, MatchMode matchMode) {
        super(expression, value);
        this.matchMode = matchMode;
    }
    
    public Like(BaseExpression expression, String value, char escapeCharacter) {
        super(expression, value);
        this.escapeCharExpressionValue = ExpressionValue.fromValue(escapeCharacter);
    }
    
    public Like(BaseExpression expression, String value, BaseExpression escapeCharacterExpression) {
        super(expression, value);
        this.escapeCharExpressionValue = ExpressionValue.fromExpression(escapeCharacterExpression);
    }
    
    public Like(BaseExpression expression, BaseExpression expressionValue) {
        super(expression, expressionValue);
    }
    
    public Like(BaseExpression expression, BaseExpression expressionValue, char escapeCharacter) {
        super(expression, expressionValue);
        this.escapeCharExpressionValue = ExpressionValue.fromValue(escapeCharacter);
    }
    
    public Like(BaseExpression expression, BaseExpression expressionValue, BaseExpression escapeCharacterExpression) {
        super(expression, expressionValue);
        this.escapeCharExpressionValue = ExpressionValue.fromExpression(escapeCharacterExpression);
    }
    
    @Override
    public Predicate toPredicate(QueryContext ctx) {
        if (expressionValue.isExpression()) {
            Expression valueExpression = expressionValue.toExpression(ctx);
            if (escapeCharExpressionValue == null) {
                return ctx.getCriteriaBuilder().like(getExpression(ctx), valueExpression);
            } else {
                if (escapeCharExpressionValue.isExpression()) {
                    return ctx.getCriteriaBuilder().like(getExpression(ctx), valueExpression, escapeCharExpressionValue.toExpression(ctx));
                } else {
                    return ctx.getCriteriaBuilder().like(getExpression(ctx), valueExpression, escapeCharExpressionValue.toValue());
                }
            }
        } else {
            String value = expressionValue.toValue();
            if (escapeCharExpressionValue == null) {
                if (matchMode == null) {
                    return ctx.getCriteriaBuilder().like(getExpression(ctx), value);
                } else {
                    if (matchMode == MatchMode.EXACT) {
                        return ctx.getCriteriaBuilder().equal(getExpression(ctx), value);
                    } else {
                        return ctx.getCriteriaBuilder().like(getExpression(ctx), matchMode.toMatchString(value));
                    }
                }
                
            } else {
                if (escapeCharExpressionValue.isExpression()) {
                    return ctx.getCriteriaBuilder().like(getExpression(ctx), value, escapeCharExpressionValue.toExpression(ctx));
                } else {
                    return ctx.getCriteriaBuilder().like(getExpression(ctx), value, escapeCharExpressionValue.toValue());
                }
            }
        }
    }
}
