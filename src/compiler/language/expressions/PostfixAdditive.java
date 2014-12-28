package compiler.language.expressions;

import compiler.lexer.Punctuator;

/**
 * Created by troy on 24/12/14.
 */
public class PostfixAdditive extends Expression {

    private Expression expr;
    private Punctuator punctuator;

    public PostfixAdditive(Expression expr, Punctuator punctuator) {
        this.expr = expr;
        this.punctuator = punctuator;
    }
}
