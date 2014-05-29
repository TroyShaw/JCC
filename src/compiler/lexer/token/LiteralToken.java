package compiler.lexer.token;

public class LiteralToken extends Token {

	private Literal type;
	private String stringValue;
	
	public LiteralToken(String stringValue, Literal type) {
		this.type = type;
		this.stringValue = stringValue;
	}
	
	public Literal getType() {
		return type;
	}
	
	public String getStringValue() {
		return stringValue;
	}
}
