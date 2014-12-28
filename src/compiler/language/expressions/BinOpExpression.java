package compiler.language.expressions;

import compiler.lexer.Punctuator;

import java.util.function.BinaryOperator;

/**
 * Created by troy on 23/12/14.
 */
public class BinOpExpression extends Expression {
    private Expression lhs, rhs;
    private Punctuator punctuator;

    public BinOpExpression(Expression lhs, Expression rhs, Punctuator punctuator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.punctuator = punctuator;
    }

    public Expression getLhs() {
        return lhs;
    }

    public Expression getRhs() {
        return rhs;
    }

    public Punctuator getPunctuator() {
        return punctuator;
    }
}
