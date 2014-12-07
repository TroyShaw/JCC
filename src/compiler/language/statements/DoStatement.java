package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * Created by troy on 14/11/14.
 */
public class DoStatement extends Statement {

    private Expression condition;
    private Statement body;

    public DoStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }
}
