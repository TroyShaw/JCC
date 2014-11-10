package compiler.lexer;

import java.util.ArrayList;
import java.util.List;

import compiler.lexer.token.Token;

/**
 * Class performs lexing on compilation units, turning them into a series of
 * tokens.
 * 
 * @author troy
 */
public class Lexer {

	private LexerBuffer b;
	private LexerHelper h;

	public Lexer(String inputProgram) {
		b = new LexerBuffer(inputProgram);
		h = new LexerHelper(b);
	}

	/**
	 * Completely lexes the program provided, returning a list of Token.
	 * 
	 * @return
	 */
	public List<Token> lex() {
		List<Token> tokens = new ArrayList<Token>();

		while (b.hasChar()) {
			if (b.tryConsume("//")) {
				b.consumeUntil("\n");
				continue;
			}
			
			if (b.tryConsume("/*")) {
				if (!b.tryConsumeUntil("*/")) syntaxError("Unfinished multiline comment");
				continue;
			}
			
			// TODO this is Java's definition of whitespace, need to use C's
			if (Character.isWhitespace(b.peekChar())) {
				b.skipWhitespace();
				continue;
			}

			Token t = scanSingleToken();

			tokens.add(t);
		}

		return tokens;
	}

	/**
	 * Scans and returns a single token.
	 * 
	 * @return
	 */
	private Token scanSingleToken() {
		if (h.tryLexCharLiteral())      return h.getToken();
		if (h.tryLexStringLiteral())    return h.getToken();
		if (h.tryLexNumericalLiteral()) return h.getToken();
		if (h.tryLexPunctuator())       return h.getToken();
		if (h.tryLexIdenOrKeyword())    return h.getToken();

		throw syntaxError("Unknown character encountered: " + b.peekChar());
	}

	private RuntimeException syntaxError(String error) {
		return new RuntimeException(error);
	}
}
