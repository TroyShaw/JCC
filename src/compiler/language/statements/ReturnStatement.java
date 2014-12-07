package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * A return statement, returning an expression (or optionally void).
 */
public class ReturnStatement extends Statement {

    private Expression toReturn;

    /**
     * Instantiates a new ReturnStatement.
     * The return expression can be null, in the case of a void function.
     *
     * @param toReturn the expression to return, can be null
     */
    public ReturnStatement(Expression toReturn) {
        this.toReturn = toReturn;
    }

    /**
     * Gets the expression to return, or null if no return value is present.
     *
     * @return the return
     */
    public Expression getReturnExpression() {
        return toReturn;
    }
}
