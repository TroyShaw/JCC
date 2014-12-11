package testing.tests.lexer;

import compiler.lexer.Lexer;
import compiler.lexer.token.Token;
import org.junit.Test;

import java.util.List;

/**
 * Created by troy on 11/12/14.
 */
public class NumberTests {

    @Test
    public void testInt() {
        String intStr = "0x123123";

        doIntTest(intStr, "23");
    }

    @Test
    public void testFloat() {
        String num1 = "123e1";
        String num2 = "123e+1";
        String num3 = "123e-1";

        String num11 = ".123e1";
        String num22 = ".123e+1";
        String num33 = ".123e-1";

        String num111 = "123.123e1";
        String num222 = "123.123e+1";
        String num333 = "123.123e-1";

        String num1111 = "123.e1";
        String num2222 = "123.e+1";
        String num3333 = "123.e-1";

        doDoubleTest(num1, "");
        doDoubleTest(num2, "");
        doDoubleTest(num3, "");

        doDoubleTest(num11, "");
        doDoubleTest(num22, "");
        doDoubleTest(num33, "");

        doDoubleTest(num111, "");
        doDoubleTest(num222, "");
        doDoubleTest(num333, "");

        doDoubleTest(num1111, "");
        doDoubleTest(num2222, "");
        doDoubleTest(num3333, "");
    }

    private void doIntTest(String intExpr, String expectedResult) {
        Lexer l = new Lexer(intExpr);
        List<Token> tokens = l.lex();

        System.out.println(tokens);

//        Assert.assertEquals(expectedResult, n.evaluate());
    }

    private void doDoubleTest(String intExpr, String expectedResult) {
        Lexer l = new Lexer(intExpr);
        List<Token> tokens = l.lex();

        System.out.println(tokens);

//        Assert.assertEquals(expectedResult, n.evaluate());
    }
}
