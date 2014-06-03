package compiler.lexer.token;

/**
 * Represents an integer constant token.
 * 
 * @author troy
 */
public class IntegerToken extends LiteralToken {

	public IntegerToken(String intString) {
		super(intString, Literal.Integer);
	}
}
