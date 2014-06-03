package compiler.lexer.token;

import compiler.lexer.Keyword;

/**
 * Represents a keyword token.
 * 
 * @author troy
 */
public class KeywordToken extends Token {

	public KeywordToken(Keyword keyword) {
		super(keyword.getString());
	}

}
