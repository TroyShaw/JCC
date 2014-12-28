package compiler.language.expressions;

/**
 * Created by troy on 24/12/14.
 */
public class SizeofExprExpression extends Expression {

    private Expression expr;

    public SizeofExprExpression(Expression expr) {
        this.expr = expr;
    }
}
