package testing.tests.parser;

import compiler.lexer.Lexer;
import compiler.lexer.token.Token;
import compiler.parser.Node;
import compiler.parser.ParserBuffer;
import compiler.parser.TestParser;
import io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by troy on 23/12/14.
 */
public class TestParserTests {

    @Test
    public void test() {
        String source = IOUtils.readFile("src/testing/code/parser/testParsing.txt");

        List<MathTestPair> testPairs = parseTestPairs(source);

        for (MathTestPair pair : testPairs) {
            Lexer l = new Lexer(pair.expr);
            List<Token> tokens = l.lex();

            ParserBuffer buffer = new ParserBuffer(tokens);
            TestParser testParser = new TestParser(buffer);

            Node n = testParser.testParse();

            Assert.assertEquals(n.evaluate(), pair.result);
        }
    }

    private static List<MathTestPair> parseTestPairs(String source) {
        List<MathTestPair> toReturn = new ArrayList<MathTestPair>();

        String[] lines = source.split(Pattern.quote("\n"));

        for (String line : lines) {
            // allow "comment" or empty lines
            if (line.startsWith("//") || line.isEmpty()) continue;

            String[] split = line.split(Pattern.quote(":"));

            String expr = split[0];
            int result = Integer.parseInt(split[1]);

            toReturn.add(new MathTestPair(expr, result));
        }

        return toReturn;
    }

    private static class MathTestPair {
        public final String expr;
        public final int result;

        public MathTestPair(String expr, int result) {
            this.expr = expr;
            this.result = result;
        }
    }
}
