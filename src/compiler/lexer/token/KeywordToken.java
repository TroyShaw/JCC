package compiler.lexer.token;

import compiler.lexer.Keyword;

/**
 * Represents a keyword token.
 * 
 * @author troy
 */
public class KeywordToken extends Token {

	private Keyword keyword;

	public KeywordToken(Keyword keyword) {
		super(keyword.getString());

		this.keyword = keyword;
	}

	public Keyword getKeyword() {
		return keyword;
	}

}
