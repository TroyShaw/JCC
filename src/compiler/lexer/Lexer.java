package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

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
 * Class performs lexing on compilation units, turning them into a series of tokens.
 * 
 * @author troy
 */
public class Lexer {
	
	private LexerBuffer h;
	
	public Lexer(String inputProgram) {
		h = new LexerBuffer(inputProgram);
	}
	
	public List<Token> lex() {
		List<Token> tokens = new ArrayList<Token>();
		
		while (h.hasChar()) {
			
			//TODO this is Java's definition of whitespace, need to use C's
            if (Character.isWhitespace(h.peekChar())) {
            	h.skipWhitespace();
            	continue;
            }
			
            tokens.add(scanSingleToken());
		}
		
		return tokens;
	}
	
	private Token scanSingleToken() {
    	char c = h.peekChar();
    	
    	if (c == '\'') return scanCharLiteral();
		if (c == '"')  return scanStringLiteral();
		if (isDigit(c)) return scanNumericLiteral();
		if (matchesOperator()) return scanOperator();
		
		//only thing left is keywords and identifiers
		return scanWord();
	}
	
	/**
	 * Scans a string literal.
	 * 
	 * @return
	 */
	private LiteralToken scanStringLiteral() {
		boolean isWide = h.tryConsume("L");
		
		h.consume("\"");
		
		StringBuffer buffer = new StringBuffer();
		
		while (h.hasChar()) {
			char c = scanChar();
			
			if (c == '"') return new StringToken(buffer.toString(), isWide);
			
			buffer.append(c);
		}

		throw syntaxError("Reached EOF while parsing string literal.");
	}

	/**
	 * Scans a character literal.
	 * 
	 * @return
	 */
	private LiteralToken scanCharLiteral() {
		boolean isWide = h.tryConsume("L");
		
		h.consume("\'");
		
		StringBuffer buffer = new StringBuffer();
		
		while (h.hasChar()) {
			char c = scanChar();
			
			if (c == '\'') return new CharacterToken(buffer.toString(), isWide);
			
			buffer.append(c);
		}

		throw syntaxError("Reached EOF while parsing char literal.");
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
		char c = h.consumeChar();
    	
    	if (c != '\\') return c;
    	
    	//we've got an escaped char
    	c = h.consumeChar();
    	
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
		
        if (h.tryConsume(".")) {
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
    	if (!h.hasChar()) throw syntaxError("Reached EOF while parsing numeric value");
    	if (!Character.isDigit(h.peekChar())) throw syntaxError("Non-numeric character found instead of digit");
    		
    	StringBuffer buffer = new StringBuffer();
    	
    	while (Character.isDigit(h.peekChar())) buffer.append(h.consumeChar());
    		
    	return buffer.toString();
    }
	
	/**
	 * Scans a c integer constant.
	 * 
	 * @return
	 */
	private LiteralToken scanIntegerConstant() {
		if (h.tryConsume("0x") || h.tryConsume("0X")) {
			//hexidecimal constant
		} else if (h.tryConsume("0")) {
			//octal constant
		} else {
			//normal integer constant
		}
		
		return null;
	}
	
	private LiteralToken scanFloatingConstant() {
		return null;
	}

	private Token scanWord() {
		//first try keywords
		
		for (Keyword k : Keyword.values()) {
			if (h.matches(k.getString())) {
				return new KeywordToken(k);
			}
		}
		
		// Must be identifier
		// Valid identifier [_a-zA-Z][_a-zA-Z0-9]*
		String identifier = "";
		
		char c = h.peekChar();
		
		if (!isIdentifierStart(c)) {
			syntaxError("invalid identifier start.");
		}
		
		identifier += c;
		
		while (isIdentifierRest(h.peekChar())) {
			identifier += h.consumeChar();
		}
		
		return new IdentifierToken(identifier);
	}
	
	private boolean matchesOperator() {
		for (Punctuator o : Punctuator.values()) {
			if (h.matches(o.getString())) return true;
		}
		
		return false;
	}
	
	private PunctuatorToken scanOperator() {
		for (Punctuator o : Punctuator.values()) {
			if (h.matches(o.getString())) {
				return new PunctuatorToken(o);
			}
		}
		
		throw syntaxError("Unknown operator");
	}
	
	/**
	 * Scans and returns the next hex string.
	 * @return
	 */
	private String scanHexString() {
		String ox;
		
		if (h.tryConsume("0x")) ox = "0x";
		else if (h.tryConsume("0X")) ox = "0x";
		else throw syntaxError("invalid hex string");
		
		//now scan digits
		char c = h.peekChar();
		
		if (!isHexDigit(c)) {
			throw syntaxError("incomplete hex string");
		}
		
		while (h.hasChar()) {
			ox += c;
			
			c = h.consumeChar();
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
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
