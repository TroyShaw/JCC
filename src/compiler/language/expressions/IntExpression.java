package compiler.language.expressions;

import compiler.lexer.token.IntegerToken;

/**
 * Created by troy on 22/12/14.
 */
public class IntExpression extends Expression {

    private IntegerToken token;

    public IntExpression(IntegerToken token) {
        this.token = token;
    }
}
