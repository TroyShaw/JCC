package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

import compiler.lexer.token.EscapeCharacter;
import compiler.lexer.token.Literal;
import compiler.lexer.token.LiteralToken;
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
		
		throw syntaxError("unknown token encountered");
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
	
	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
