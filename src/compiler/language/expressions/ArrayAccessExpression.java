package compiler.language.expressions;

/**
 * Created by troy on 24/12/14.
 */
public class ArrayAccessExpression extends Expression {

    private Expression expr;
    private Expression index;

    public ArrayAccessExpression(Expression expr, Expression index) {
        this.expr = expr;
        this.index = index;
    }
}
