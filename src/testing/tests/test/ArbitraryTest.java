package testing.tests.test;

import compiler.lexer.Lexer;
import compiler.lexer.token.Token;
import io.IOUtils;
import org.junit.Test;

import java.util.List;

/**
 * Created by troy on 21/12/14.
 */
public class ArbitraryTest {

    @Test
    public void arbitraryTest() {
        String source = IOUtils.readFile("src/testing/code/test/test.c");

        Lexer l = new Lexer(source);

        List<Token> tokens = l.lex();

        System.out.println(tokens);
    }
}
