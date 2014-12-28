package compiler.parser;

import java.util.ArrayList;
import java.util.List;

import compiler.language.declarations.Declaration;
import compiler.language.expressions.Expression;
import compiler.language.statements.Statement;
import compiler.lexer.LexerBuffer;
import compiler.lexer.token.Token;

/**
 * Class handles parsing of a list of tokens into its abstract syntax tree.
 * 
 * @author troy
 */
public class Parser {

	private List<Token> tokens;
	private ParserBuffer b;

	private DeclarationParser declarationParser;
	private ExpressionParser expressionParser;
	private StatementParser statementParser;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		b = new ParserBuffer(tokens);

		declarationParser = new DeclarationParser(this, b);
		expressionParser = new ExpressionParser(this, b);
		statementParser = new StatementParser(this, b);
	}
	
	public List<Statement> parse() {
		List<Statement> statements = new ArrayList<Statement>();

		while (b.hasToken()) {
				statements.add(statementParser.parseStatement());
		}

		return statements;
	}

	public Expression parseExpression() {
		return expressionParser.parseExpression();
	}

	public Declaration parseDeclaration() {
		return declarationParser.parseDeclaration();
	}

	public Statement parseStatement() {
		return statementParser.parseStatement();
	}
}
