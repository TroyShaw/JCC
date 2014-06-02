package compiler.lexer;

import compiler.lexer.token.CharacterToken;
import compiler.lexer.token.EscapeCharacter;
import compiler.lexer.token.FloatingToken;
import compiler.lexer.token.IdentifierToken;
import compiler.lexer.token.IntegerToken;
import compiler.lexer.token.KeywordToken;
import compiler.lexer.token.LiteralToken;
import compiler.lexer.token.PunctuatorToken;
import compiler.lexer.token.StringToken;
import compiler.lexer.token.Token;

/**
 * Class provides helper methods to lex certain language elements.
 * 
 * @author troy
 */
public class LexerHelper {

	private Token lastLexedToken;
	
	private LexerBuffer b;
	
	public LexerHelper(LexerBuffer buffer) {
		this.b = buffer;
	}
	
	/**
	 * Returns the last lexed token.
	 * 
	 * @return
	 */
	public Token getToken() {
		return lastLexedToken;
	}
	
	/**
	 * Tries to lex a string literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexStringLiteral() {
		if (!b.matches("\"", "L'")) return false;
		
		boolean isWide = b.tryConsume("L");
		
		b.consume("\"");
		
		StringBuffer buffer = new StringBuffer();
		
		while (b.hasChar()) {
			char c = scanChar();
			
			if (c == '"') return setToken(new StringToken(buffer.toString(), isWide));
			
			buffer.append(c);
		}
		
		return false;
	}
	
	/**
	 * Tries to lex a character literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexCharLiteral() {
		if (!b.matches("\'", "L'")) return false;
		
		boolean isWide = b.tryConsume("L");
		
		b.consume("\'");
		
		StringBuffer buffer = new StringBuffer();
		
		while (b.hasChar()) {
			char c = scanChar();
			
			if (c == '\'') return setToken(new CharacterToken(buffer.toString(), isWide));
			
			buffer.append(c);
		}
		
		return false;
	}
	
	/**
	 * Tries to lex a numerical literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexNumericalLiteral() {
		return false;
	}
	
	/**
	 * Tries to lex an identifier or keyword, returning true if this succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexIdenOrKeyword() {
		if (!isIdentifierStart(b.peekChar())) return false;
		
		String word = matchIdentifier();
		
		// Check if the word is a keyword
		for (Keyword k : Keyword.values()) {
			if (word.equals(k.getString())) {
				return setToken(new KeywordToken(k));
			}
		}
		
		//Otherwise it must be an identifier
		return setToken(new IdentifierToken(word));
	}
	
	
	/**
	 * Tries to lex a punctuator, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexPunctuator() {
		for (Punctuator o : Punctuator.values()) {
			if (b.matches(o.getString())) {
				return setToken(new PunctuatorToken(o));
			}
		}
		
		return false;
	}
	
	/**
	 * Scans tokens until a non-identifier token is found, returning the identifier
	 * formed in this process.
	 * 
	 * This method assumes at least 1 character is in the buffer that can start the
	 * identifier.
	 * 
	 * @return
	 */
	private String matchIdentifier() {
		StringBuffer identifierBuffer = new StringBuffer();
		
		while (isIdentifierRest(b.peekChar())) {
			identifierBuffer.append(b.consumeChar());
		}
		
		return identifierBuffer.toString();
	}
	
	 /**
     * Scans the next char in the input, consuming and returning it.
     * 
     * Note: this isn't for scanning a char representation in source code, e.g. 'a'
     * This simply scans a single char and returns it, e.g. a
     * 
     * Handles escape characters properly, consuming 2 chars for an escaped char.
     * If EOF is reached, a syntax error is thrown.
     * 
     * @return
     */
	private char scanChar() {
		char c = b.consumeChar();
    	
    	if (c != '\\') return c;
    	
    	//we've got an escaped char
    	c = b.consumeChar();
    	
    	for (EscapeCharacter esChar : EscapeCharacter.values()) {
    		if (c == esChar.getCharValue()) {
    			return esChar.getEscapedChar();
    		}
    	}
    	
    	//TODO: unicode, octal, hex constants, etc
    	
    	throw syntaxError("unrecognised escape character");
	}
	
	/**
	 * Scans a numerical literal.
	 * 
	 * @return
	 */
	private LiteralToken scanNumericLiteral() {
		String firstNum = scanNumeric();
		
        if (b.tryConsume(".")) {
        	String secondNum = scanNumeric();
        	
            return new FloatingToken(firstNum + "." + secondNum);
        } else {
        	return new IntegerToken(firstNum);
        }
	}
	
    /**
     * Scans from the current character until the last that isn't a number.
     * These characters are concatenated and returned.
     * 
     * The lexer-helpers internal offset is moved by calling this function.
     * 
     * If no number can be made, an exception is thrown.
     * 
     * @return
     */
    private String scanNumeric() {
    	if (!b.hasChar()) throw syntaxError("Reached EOF while parsing numeric value");
    	if (!Character.isDigit(b.peekChar())) throw syntaxError("Non-numeric character found instead of digit");
    		
    	StringBuffer buffer = new StringBuffer();
    	
    	while (Character.isDigit(b.peekChar())) buffer.append(b.consumeChar());
    		
    	return buffer.toString();
    }
	
	/**
	 * Scans a c integer constant.
	 * 
	 * @return
	 */
	private LiteralToken scanIntegerConstant() {
		if (b.tryConsume("0x") || b.tryConsume("0X")) {
			//hexidecimal constant
		} else if (b.tryConsume("0")) {
			//octal constant
		} else {
			//normal integer constant
		}
		
		return null;
	}	
	
	/**
	 * Scans and returns the next hex string.
	 * @return
	 */
	private String scanHexString() {
		String ox;
		
		if (b.tryConsume("0x")) ox = "0x";
		else if (b.tryConsume("0X")) ox = "0x";
		else throw syntaxError("invalid hex string");
		
		//now scan digits
		char c = b.peekChar();
		
		if (!isHexDigit(c)) {
			throw syntaxError("incomplete hex string");
		}
		
		while (b.hasChar()) {
			ox += c;
			
			c = b.consumeChar();
		}
		
		
		throw syntaxError("Reached EOF while parsing hex");
	}
	
	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}
	
	private boolean isIdentifierStart(char c) {
		return c == '_' || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}
	
	private boolean isHexDigit(char c) {
		return isDigit(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
	}
	
	private boolean isIdentifierRest(char c) {
		return isIdentifierStart(c) || isDigit(c);
	}
	
	/**
	 * Private helper method which sets the internal token to the given
	 * value then returns true.
	 * 
	 * @param t
	 * @return
	 */
	private boolean setToken(Token token) {
		lastLexedToken = token;
		
		return true;
	}
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
