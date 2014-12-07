package compiler.parser;

import compiler.lexer.Punctuator;
import compiler.lexer.token.*;

import java.util.List;

/**
 * Created by troy on 15/11/14.
 */
public class ExpressionParser {

    private int index = 0;
    private List<Token> tokens;

    public ExpressionParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node parseMath() {
        return parseAdd();
    }

    private Node parseAddOld() {
        Node lhs = parseMult();

        if (tryMatchPunctuator(Punctuator.Plus)) {
            Node rhs = parseMath();

            return new Node.AddNode(lhs, rhs);
        } if (tryMatchPunctuator(Punctuator.Minus)) {
            Node rhs = parseMath();

            return new Node.MinusNode(lhs, rhs);
        } else {
            return lhs;
        }
    }

    private Node parseAdd() {
        Node lhs = parseMult();

        Punctuator p;
        while ((p = tryMatchPunctuators(Punctuator.Plus, Punctuator.Minus)) != null) {
            //Node rhs = parseMath();
            Node rhs = parseMult();

            lhs = new Node.OperatorNode(lhs, rhs, p);
        }

        return lhs;
    }

    private Node parseMult() {
        Node lhs = parseParen();

        Punctuator p;
        while ((p = tryMatchPunctuators(Punctuator.Asterisk, Punctuator.Slash)) != null) {
            //Node rhs = parseMath();
            Node rhs = parseParen();

            lhs = new Node.OperatorNode(lhs, rhs, p);
        }

//        if (tryMatchPunctuator(Punctuator.Asterisk)) {
//            Node rhs = parseMath();
//
//            return new Node.MultNode(lhs, rhs);
//        } if (tryMatchPunctuator(Punctuator.Slash)) {
//            Node rhs = parseMath();
//
//            return new Node.DivideNode(lhs, rhs);
//        }  else {
//            return lhs;
//        }

        return lhs;
    }

    private Node parseAssign() {
        return null;
    }

    private Node parseParen() {
        if (tryMatchPunctuator(Punctuator.LeftParenthesis)) {
            Node e = parseMath();
            matchPunctuator(Punctuator.RightParenthesis);

            return e;
        } else {
            return getNumNode();
        }
    }

    private Node parsePrimary() {
        Token n = consumeToken();

        if (n instanceof IntegerToken) {

        } else if (n instanceof FloatingToken) {

        } else if (n instanceof CharacterToken) {

        } else if (n instanceof IdentifierToken) {

        } else if (n instanceof StringToken) {

        } else if (tryMatchPunctuator(Punctuator.LeftParenthesis)) {

        }

        return null;
    }

    private boolean tryMatchPunctuator(Punctuator punctuator) {
        if (!hasToken()) return false;

        Token t = peekToken();

        if (t instanceof PunctuatorToken) {
            PunctuatorToken puncToken = (PunctuatorToken) t;

            if (puncToken.matches(punctuator)) {
                consumeToken();

                return true;
            }
        }

        return false;
    }

    private void matchPunctuator(Punctuator punctuator) {
        if (!tryMatchPunctuator(punctuator)) {
            throw new RuntimeException("Should have matched punctuator: " + punctuator);
        }
    }

    public Punctuator tryMatchPunctuators(Punctuator ... punctuators) {
        for (Punctuator p : punctuators) {
            if (tryMatchPunctuator(p)) return p;
        }

        return null;
    }

    private Node getNumNode() {
        Token t = consumeToken();

        if (t instanceof LiteralToken) {
            LiteralToken litTok = (LiteralToken) t;

            int i = Integer.parseInt(litTok.getStringValue());

            return new Node.IntNode(i);
        }

        throw new RuntimeException("Should have matched against a literal node, matched against: " + t);
    }

    private Token consumeToken() {
        return tokens.get(index++);
    }

    private Token peekToken() {
        return tokens.get(index);
    }

    private boolean hasToken() {
        return index < tokens.size();
    }
}
