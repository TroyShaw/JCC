package compiler.lexer.token;

public class CharacterToken extends LiteralToken {

	public CharacterToken(String characterValue, boolean isWide) {
		super(characterValue, Literal.Character);
	}

}
