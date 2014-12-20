package compiler.lexer.token;

import compiler.lexer.token.cString.CCharSequence;

/**
 * Class defines a character constant token.
 * 
 * @author troy
 */
public class CharacterToken extends LiteralToken {

	public CharacterToken(CCharSequence charSequence) {
		super(charSequence.getString(), Literal.Character);
	}

}
