package compiler.lexer.token;

/**
 * Class defines a string constant token.
 * 
 * @author troy
 */
public class StringToken extends LiteralToken {

	public StringToken(String stringValue, boolean isWide) {
		super(stringValue, Literal.String);
	}

}
