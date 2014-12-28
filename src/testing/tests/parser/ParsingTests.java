package testing.tests.parser;

import compiler.language.statements.Statement;
import compiler.lexer.Lexer;
import compiler.lexer.token.Token;
import compiler.parser.Parser;
import io.IOUtils;
import org.junit.Test;

import java.util.List;

/**
 * Created by troy on 21/12/14.
 */
public class ParsingTests {

    @Test
    public void parsingTest() {
        String source = IOUtils.readFile("src/testing/code/parser/test1.c");

        Lexer l = new Lexer(source);

        List<Token> tokens = l.lex();
        System.out.println(tokens);

        Parser p = new Parser(tokens);
        List<Statement> statements = p.parse();

        System.out.println(tokens);
    }
}
