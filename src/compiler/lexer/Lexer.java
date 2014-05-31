package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

import compiler.lexer.token.EscapeCharacter;
import compiler.lexer.token.IdentifierToken;
import compiler.lexer.token.KeywordToken;
import compiler.lexer.token.Literal;
import compiler.lexer.token.LiteralToken;
import compiler.lexer.token.OperatorToken;
import compiler.lexer.token.Token;


/**
 * Class performs lexing on compilation units, turning them into a series of tokens.
 * 
 * @author troy
 */
public class Lexer {
	
	private LexerHelper h;
	
	public Lexer(String inputProgram) {
		h = new LexerHelper(inputProgram);
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
	
	private LiteralToken scanStringLiteral() {
		h.consume("\"");
		
		StringBuffer buffer = new StringBuffer();
		
		while (h.hasChar()) {
			char c = scanChar();
			
			if (c == '"') return new LiteralToken(buffer.toString(), Literal.String);
			
			buffer.append(c);
		}

		throw syntaxError("Reached EOF while parsing string literal.");
	}

	/**
	 * Scans a c char literal.
	 * 
	 * @return
	 */
	private LiteralToken scanCharLiteral() {
		h.consume("'");
		char c = scanChar();
		h.consume("'");
		
		return new LiteralToken(Character.toString(c), Literal.Character);
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
    	
    	throw syntaxError("unrecognised escape character");
	}
	
	private LiteralToken scanNumericLiteral() {
		
		//TODO
		return null;
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
		for (Operator o : Operator.values()) {
			if (h.matches(o.getString())) return true;
		}
		
		return false;
	}
	
	private OperatorToken scanOperator() {
		for (Operator o : Operator.values()) {
			if (h.matches(o.getString())) {
				return new OperatorToken(o);
			}
		}
		
		throw syntaxError("Unknown operator ");
	}
	
	private boolean isDigit(char c) {
		return '0' <= c && c <= '9';
	}
	
	private boolean isIdentifierStart(char c) {
		return c == '_' || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}
	
	private boolean isIdentifierRest(char c) {
		return isIdentifierStart(c) || isDigit(c);
	}
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
