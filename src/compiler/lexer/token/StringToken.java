package compiler.lexer.token;

import compiler.lexer.token.cString.CCharSequence;

/**
 * Class defines a string constant token.
 * 
 * @author troy
 */
public class StringToken extends LiteralToken {

	public StringToken(CCharSequence charSequence) {
		super(charSequence.getString(), Literal.String);
	}

}
