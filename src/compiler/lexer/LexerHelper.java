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
 * To use this class, call one of the tryLex*() methods.
 * The method will attempt to lex the given token, returning true if it succeeds.
 * If a lex occurs, one should then call getToken() to access this saved lexed token.
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
			char c = scanSimpleChar();
			
			if (c == '"') return setToken(new StringToken(buffer.toString(), isWide));
			
			buffer.append(c);
		}
		
		throw syntaxError("Reached EOF while parsing string literal");
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
			char c = scanSimpleChar();
			
			if (c == '\'') return setToken(new CharacterToken(buffer.toString(), isWide));
			
			buffer.append(c);
		}
		
		throw syntaxError("Reached EOF while parsing char literal");
	}
	
	/**
	 * Tries to lex a numerical literal, returning true if it succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexNumericalLiteral() {
		if (!isNumericalStart()) return false;
		
		// Floating point constants always start with the form
		// [digit-sequence] . [digit sequence]     or
		// digit-sequence . [digit sequence]
		// So we can tell a floating point if we have either a decimal, or a 
		// decimal after a single dot
		
		String lhs = scanDecimalNumber();
		
		if (b.tryConsume(".")) {
			//a floating point value
			String rhs = scanDecimalNumber();
			
			return setToken(new FloatingToken(lhs + "." + rhs));
		} else {
			//was a simple integer
			return setToken(new IntegerToken(lhs));
		}
	}
	
	/**
	 * Tries to lex an identifier or keyword, returning true if this succeeds.
	 * 
	 * @return
	 */
	public boolean tryLexIdenOrKeyword() {
		if (!b.matches(CharType.IdentifierStart)) return false;
		
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
			if (b.tryConsume(o.getString())) {
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
		return b.nextMatching(CharType.IdentifierRest);
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
	private char scanSimpleChar() {
		char c = b.consumeChar();
    	
    	if (c != '\\') return c;
    	
    	//we've got an escaped char
    	c = b.consumeChar();
    	
    	for (EscapeCharacter esChar : EscapeCharacter.values()) {
    		if (c == esChar.getCharValue()) {
    			return esChar.getEscapedChar();
    		}
    	}
    	
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
		
		if (!CharType.Hex.matches(c)) {
			throw syntaxError("incomplete hex string");
		}
		
		return ox + scanHexNumber();
	}

	private String scanDecimalNumber() {
		return b.nextMatching(CharType.Decimal);
	}
	
	private String scanOctalNumber() {
		return b.nextMatching(CharType.Octal);
	}
	
	private String scanHexNumber() {
		return b.nextMatching(CharType.Hex);
	}
	
	/**
	 * Scans a single universal character name.
	 * 
	 * This assumes a valid UCN is currently present in the buffer.
	 * 
	 * @return
	 */
	private String scanUniversalCharacterName() {
		String ox;
		
		if (b.tryConsume("\\u")) ox = "\\u";
		else if (b.tryConsume("\\u")) ox = "\\u";
		else throw syntaxError("invalid UCN string");
		
		char c;
		
		//first match the guaranteed 4
		for (int i = 0; i < 4; i++) {
			c = b.peekChar();
			
			if (!CharType.Hex.matches(c)) {
				throw syntaxError("UCN requires at least 4 hex digits");
			}
			
			ox += b.consumeChar();
		}
		
		//now we must try match 4. If a non-hex is found, we must revert our scan
		for (int i = 0; i < 4; i++) {
			
		}
		
		return null;
	}
	
	/**
	 * Returns true if the current buffer is the start of a numerical constant.
	 * 
	 * @return
	 */
	private boolean isNumericalStart() {
		// A numerical value either starts with a digit, or a dot followed by a digit
		if (b.matches(CharType.Decimal)) return true;
		
		if (b.peekChar() == '.') {
			// Check the next digit is numerical. Be sure the preserve the buffer state.
			b.consumeChar();
			char c = b.peekChar();
			b.rewind();
			
			return CharType.Decimal.matches(c);
		} else {
			return false;
		}
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
