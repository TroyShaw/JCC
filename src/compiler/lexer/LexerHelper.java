package compiler.lexer;

import compiler.lexer.token.Token;

/**
 * Class provides helper methods to lex certain language elements.
 * 
 * @author troy
 */
public class LexerHelper {

	private Token lastLexedToken;
	
	public boolean tryLexNumericalLiteral() {
		return false;
	}
	
	/**
	 * Attempts to lex a
	 * @return
	 */
	public boolean tryLexNothing() {
		return false;
	}
	
	/**
	 * Returns the last lexed token.
	 * 
	 * @return
	 */
	public Token getToken() {
		return lastLexedToken;
	}
}
