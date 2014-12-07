package compiler.lexer.token;

import compiler.lexer.Punctuator;

/**
 * Class defines a punctuator token.
 * 
 * @author troy
 */
public class PunctuatorToken extends Token {

    private Punctuator operator;

	public PunctuatorToken(Punctuator operator) {
		super(operator.getString());

        this.operator = operator;
	}

    public Punctuator getPunctuator() {
        return operator;
    }

    public boolean matches(Punctuator punctuator) {
        return punctuator == operator;
    }
}
