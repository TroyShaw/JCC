package compiler.lexer.token;

/**
 * Class defines a floating-point constant token.
 * 
 * @author troy
 */
public class FloatingToken extends LiteralToken {


	public FloatingToken(String floatingValue) {
		super(floatingValue, Literal.Floating);
	}

}
