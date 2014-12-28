package compiler.language.expressions;

/**
 * Created by troy on 24/12/14.
 */
public class CastExpression extends Expression {

    private Expression type;
    private Expression toCast;

    public CastExpression(Expression type, Expression toCast) {
        this.type = type;
        this.toCast = toCast;
    }
}
