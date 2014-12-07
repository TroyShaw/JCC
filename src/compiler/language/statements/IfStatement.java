package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * Created by troy on 14/11/14.
 */
public class IfStatement extends Statement {

    private Expression condition;
    private Statement ifBody, elseBody;

    public IfStatement(Expression condition, Statement ifBody, Statement elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }
}
