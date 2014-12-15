package compiler.lexer.token;

/**
 * Class defines a floating-point constant token.
 * 
 * @author troy
 */
public class FloatingToken extends LiteralToken {

    public FloatingToken(String lhs, String rhs, int exponentSign, String exponent, String floatingSuffix, NumericType numericType) {
        super("", Literal.Floating);
    }
}
