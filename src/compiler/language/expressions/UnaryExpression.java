package compiler.language.expressions;

import compiler.lexer.Punctuator;

/**
 * Created by troy on 24/12/14.
 */
public class UnaryExpression extends Expression {

    private Expression expr;
    private Punctuator operation;

    public UnaryExpression(Expression expr, Punctuator operation) {
        this.expr = expr;
        this.operation = operation;
    }
}
