package compiler.parser;

import java.util.List;

import compiler.lexer.token.Token;

/**
 * Class handles parsing of a list of tokens into its abstract syntax tree.
 * 
 * @author troy
 */
public class Parser {

	private List<Token> tokens;
	
	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public CAST parse() {
		return null;
	}
}
