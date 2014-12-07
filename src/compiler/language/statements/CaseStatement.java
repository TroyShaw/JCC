package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * Created by troy on 14/11/14.
 */
public class CaseStatement extends Statement {

    private Expression caseExpr;
    private Statement body;

    public CaseStatement(Expression caseExpr, Statement body) {
        this.caseExpr = caseExpr;
        this.body = body;
    }
}
