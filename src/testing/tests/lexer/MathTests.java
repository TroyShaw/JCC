package testing.tests.lexer;

import compiler.lexer.Lexer;
import compiler.lexer.Punctuator;
import compiler.lexer.token.PunctuatorToken;
import compiler.lexer.token.Token;
import compiler.parser.ExpressionParser;
import compiler.parser.Node;
import io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by troy on 15/11/14.
 */
public class MathTests {

    @Test
    public void addTest1() {
        String source = "1";

        doTest(source, 1);
    }

    @Test
    public void addTest2() {
        String source = "1 + 2";

        doTest(source, 3);
    }

    @Test
    public void addTest3() {
        String source = "1 + 2 + 3 + 4 + 5 + (6 + 7)";

        doTest(source, 28);
    }

    @Test
    public void multTest1() {
        String source = "3 * 5";

        doTest(source, 15);
    }

    @Test
    public void multTest2() {
        String source = "1 + 4 * 5";

        doTest(source, 21);
    }

    @Test
    public void multTest3() {
        String source = "3 + 10 * 12 / 5 + 2";

        doTest(source, 29);
    }

    private void doTest(String mathExpr, int expectedResult) {
        Lexer l = new Lexer(mathExpr);
        List<Token> tokens = l.lex();

        ExpressionParser exParser = new ExpressionParser(tokens);
        Node n = exParser.parseMath();

        Assert.assertEquals(expectedResult, n.evaluate());
    }
}
