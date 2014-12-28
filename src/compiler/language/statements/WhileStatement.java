package compiler.language.statements;

import compiler.language.expressions.Expression;

/**
 * Created by troy on 14/11/14.
 */
public class WhileStatement extends Statement {

    private Expression condition;
    private Statement body;

    public WhileStatement(Expression condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }


    public String output() {
        StringBuilder builder = new StringBuilder();

        builder.append("while (").append(condition.toString()).append(")").append(";");
        builder.append(body.output());

        return builder.toString();
    }
}
