package compiler.lexer.token;

/**
 * Class defines an abstract base for the different literal tokens in a C99 program.
 * 
 * @author troy
 */
public abstract class LiteralToken extends Token {

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
