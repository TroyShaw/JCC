package compiler.language.expressions;

import compiler.lexer.Punctuator;

/**
 * Created by troy on 23/12/14.
 */
public class AssignmentExpression extends Expression {

    private Expression lhs, rhs;
    private Punctuator punctuator;

    public AssignmentExpression(Expression lhs, Expression rhs, Punctuator punctuator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.punctuator = punctuator;
    }
}
