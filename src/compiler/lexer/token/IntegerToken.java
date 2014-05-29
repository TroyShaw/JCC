package compiler.lexer.token;

public class IntegerToken extends LiteralToken {

	public String intString;
	
	public IntegerToken(String intString) {
		super(intString, Literal.Integer);
	}
}
