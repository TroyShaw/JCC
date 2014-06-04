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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Token) {
			Token t = (Token) o;
			
			boolean sameClass = o.getClass().equals(getClass());
			boolean sameValue = t.string.equals(string);
			
			return sameClass && sameValue;
		}
		
		return false;
	}
}
