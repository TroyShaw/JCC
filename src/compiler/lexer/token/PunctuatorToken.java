package compiler.lexer.token;

import compiler.lexer.Punctuator;

/**
 * Class defines a punctuator token.
 * 
 * @author troy
 */
public class PunctuatorToken extends Token {

	public PunctuatorToken(Punctuator operator) {
		super(operator.getString());
	}

}
