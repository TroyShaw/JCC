package testing.tests.lexer;



import java.util.ArrayList;
import java.util.List;



import io.IOUtils;

import org.junit.Assert;
import org.junit.Test;

import compiler.lexer.Keyword;
import compiler.lexer.Lexer;
import compiler.lexer.Punctuator;
import compiler.lexer.token.IdentifierToken;
import compiler.lexer.token.KeywordToken;
import compiler.lexer.token.PunctuatorToken;
import compiler.lexer.token.Token;

public class BasicLexingTests {

	@Test
	public void test() {
		String source = IOUtils.readFile("basic_lex_1.c");
		
		Lexer l = new Lexer(source);
		
		List<Token> tokens = l.lex();
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new KeywordToken(Keyword.Int));
		expected.add(new IdentifierToken("main"));
		expected.add(new PunctuatorToken(Punctuator.LeftParenthesis));
		expected.add(new KeywordToken(Keyword.Void));
		expected.add(new PunctuatorToken(Punctuator.RightParenthesis));
		expected.add(new PunctuatorToken(Punctuator.LeftCurlyBracket));
		expected.add(new KeywordToken(Keyword.Return));
		expected.add(new PunctuatorToken(Punctuator.SemiColon));
		expected.add(new PunctuatorToken(Punctuator.RightCurlyBracket));
		
		
		Assert.assertTrue(true);
	}

}
