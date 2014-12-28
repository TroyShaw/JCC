package compiler.parser;

import compiler.lexer.Punctuator;
import compiler.lexer.token.IntegerToken;
import compiler.lexer.token.Token;

/**
 * Created by troy on 23/12/14.
 */
public class TestParser {

    private ParserBuffer b;

    public TestParser(ParserBuffer b) {
        this.b = b;
    }

    public Node testParse() {
        return parseAdditive();
    }

    private Node parseAdditive() {
        Node lhs = parseMultiplicative();

        while (b.tryConsume(Punctuator.Plus, Punctuator.Minus)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Node rhs = parseMultiplicative();
            lhs = new Node.OperatorNode(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Node parseMultiplicative() {
        Node lhs = getIntNode();

        while (b.tryConsume(Punctuator.Asterisk, Punctuator.Slash)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Node rhs = getIntNode();
            lhs = new Node.OperatorNode(lhs, rhs, punctuator);
        }

        return lhs;
    }

    public Node getIntNode() {
        Token t = b.consume();

        if (t instanceof IntegerToken) {
            IntegerToken intToken = (IntegerToken) t;

            return new Node.IntNode(Integer.parseInt(intToken.getStringValue()));
        }

        throw new RuntimeException("Expected int token, got: " + t);
    }
}
