package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * Created by troy on 14/11/14.
 */
public class SwitchStatement extends Statement {

    private Expression switchExpr;
    private Statement body;

    public SwitchStatement(Expression switchExpr, Statement body) {
        this.switchExpr = switchExpr;
        this.body = body;
    }
}
