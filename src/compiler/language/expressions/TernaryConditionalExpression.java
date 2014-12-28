package compiler.language.expressions;

/**
 * Created by troy on 23/12/14.
 */
public class TernaryConditionalExpression extends Expression {

    private Expression condition;
    private Expression lhs, rhs;

    public TernaryConditionalExpression(Expression condition, Expression lhs, Expression rhs) {
        this.condition = condition;
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
