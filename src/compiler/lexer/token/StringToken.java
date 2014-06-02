package compiler.lexer.token;

public class StringToken extends LiteralToken {

	public StringToken(String stringValue, boolean isWide) {
		super(stringValue, Literal.String);
	}

}
