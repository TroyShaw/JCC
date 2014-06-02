package compiler.lexer.token;

public class FloatingToken extends LiteralToken {


	public FloatingToken(String floatingValue) {
		super(floatingValue, Literal.Floating);

	}

}
