package compiler.language.expressions;

/**
 * Created by troy on 23/12/14.
 */
public class CommaExpression extends Expression {

    private Expression lhs, rhs;

    public CommaExpression(Expression lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
