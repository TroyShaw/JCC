package compiler.lexer.token;

/**
 * Class defines a character constant token.
 * 
 * @author troy
 */
public class CharacterToken extends LiteralToken {

	public CharacterToken(String characterValue, boolean isWide) {
		super(characterValue, Literal.Character);
	}

}
