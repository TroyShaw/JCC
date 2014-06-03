package compiler.lexer.token;

/**
 * Represents a single token in a c compilation unit.
 * 
 * @author troy
 */
public abstract class Token {

	private String string;
	
	public Token() {
		//TODO: remove this?
	}
	
	public Token(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return "{Token. Type: " + getClass() + ", value: " + string + " }";
	}
}
