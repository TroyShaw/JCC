package compiler.parser;

import compiler.language.expressions.*;
import compiler.lexer.Keyword;
import compiler.lexer.Punctuator;
import compiler.lexer.token.*;

import java.util.List;

/**
 * Created by troy on 15/11/14.
 */
public class ExpressionParser {

    private Parser parentParser;
    private ParserBuffer b;

    public ExpressionParser(Parser parentParser, ParserBuffer b) {
        this.parentParser = parentParser;
        this.b = b;
    }

    public Expression parseExpression() {
        return parseCommaExpression();
    }

    private Expression parseCommaExpression() {
        Expression lhs = parseAssignment();

        while (b.tryConsume(Punctuator.Comma)) {
            Expression rhs = parseCommaExpression();

            lhs = new CommaExpression(lhs, rhs);
        }

        return lhs;
    }

    private Expression parseAssignment() {
        Expression lhs = parseTernaryConditional();

        Punctuator[] punctuators = {
                Punctuator.Equal,
                Punctuator.MultiplyAssign, Punctuator.DivideAssign, Punctuator.RemainderAssign,
                Punctuator.PlusAssign, Punctuator.MinusAssign,
                Punctuator.LeftShiftAssign, Punctuator.RightShiftAssign,
                Punctuator.BitwiseAndAssign, Punctuator.BitwiseOrAssign, Punctuator.BitwiseXorAssign
        };

        if (b.tryConsume(punctuators)) {
            Punctuator p = b.lastMatchedPunctuator();

            Expression rhs = parseAssignment();

            return new AssignmentExpression(lhs, rhs, p);
        }

        return lhs;
    }

    private Expression parseTernaryConditional() {
        Expression conditionalExpr = parseLogicalOr();

        if (b.tryConsume(Punctuator.QuestionMark)) {
            Expression lhs = parseTernaryConditional();
            b.consume(Punctuator.Colon);
            Expression rhs = parseTernaryConditional();

            return new TernaryConditionalExpression(conditionalExpr, lhs, rhs);
        }

        return conditionalExpr;
    }

    private Expression parseLogicalOr() {
        Expression lhs = parseLogicalAnd();

        while (b.tryConsume(Punctuator.LogicalOr)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseLogicalAnd();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseLogicalAnd() {
        Expression lhs = parseBitwiseOr();

        while (b.tryConsume(Punctuator.LogicalAnd)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseBitwiseOr();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseBitwiseOr() {
        Expression lhs = parseBitwiseXor();

        while (b.tryConsume(Punctuator.Bar)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseBitwiseXor();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseBitwiseXor() {
        Expression lhs = parseBitwiseAnd();

        while (b.tryConsume(Punctuator.Caret)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseBitwiseAnd();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseBitwiseAnd() {
        Expression lhs = parseEquality();

        while (b.tryConsume(Punctuator.Ampersand)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseEquality();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseEquality() {
        Expression lhs = parseRelational();

        while (b.tryConsume(Punctuator.EqualEqual, Punctuator.NotEqual)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseRelational();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseRelational() {
        Expression lhs = parseShift();

        Punctuator[] relational = {
            Punctuator.GreaterThan, Punctuator.GreaterThanEq,
            Punctuator.LessThan, Punctuator.LessThanEq
        };

        while (b.tryConsume(relational)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseShift();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseShift() {
        Expression lhs = parseAdditive();

        while (b.tryConsume(Punctuator.LeftShift, Punctuator.RightShift)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseAdditive();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseAdditive() {
        Expression lhs = parseMultiplicative();

        while (b.tryConsume(Punctuator.Plus, Punctuator.Minus)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseMultiplicative();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseMultiplicative() {
        Expression lhs = parseCast();

        while (b.tryConsume(Punctuator.Asterisk, Punctuator.Slash)) {
            Punctuator punctuator = b.lastMatchedPunctuator();

            Expression rhs = parseCast();
            lhs = new BinOpExpression(lhs, rhs, punctuator);
        }

        return lhs;
    }

    private Expression parseCast() {
        if (b.tryConsume(Punctuator.LeftParenthesis)) {
            // TODO, consume a type here
            Expression typeName = null;
            b.consume(Punctuator.RightParenthesis);

            Expression toCast = parseUnary();

            return new CastExpression(typeName, toCast);
        }

        return parseUnary();
    }

    private Expression parseUnary() {
        Expression lhs = parsePostfix();

        if (b.tryConsume(Punctuator.PlusPlus, Punctuator.MinusMinus)) {
            Punctuator p = b.lastMatchedPunctuator();

            Expression expr = parsePostfix();

            return new PrefixAdditive(expr, p);
        }

        Punctuator[] unaryOperators = new Punctuator[] {
                Punctuator.Ampersand, Punctuator.Asterisk,
                Punctuator.Plus, Punctuator.Minus,
                Punctuator.Tilde, Punctuator.LogicalNot
        };

        if (b.tryConsume(unaryOperators)) {
            Punctuator p = b.lastMatchedPunctuator();

            Expression expr = parsePostfix();

            return new UnaryExpression(expr, p);
        }

        if (b.tryConsume(Keyword.Sizeof)) {
            // This isn't quite right
            // It is trying to parse a sizeof (typename)
            // Except we could have a parenthesized expression here too...
            // Need to figure out a way to try parse just a type name
            if (b.tryConsume(Punctuator.LeftParenthesis)) {
                // TODO, consume a type-name here
                Expression typeName = null;

                b.consume(Punctuator.RightParenthesis);

                return new SizeofTypeExpression(typeName);
            }

            Expression expr = parseUnary();

            return new SizeofExprExpression(expr);
        }

        return lhs;
    }

    private Expression parsePostfix() {
        Expression lhs = parsePrimary();

        // Array index access
        if (b.tryConsume(Punctuator.LeftSquareBracket)) {
            Expression index = parsePostfix();
            b.consume(Punctuator.RightSquareBracket);

            return new ArrayAccessExpression(lhs, index);
        }

        // Function call with parameters
        if (b.tryConsume(Punctuator.LeftParenthesis)) {

        }

        if (b.tryConsume(Punctuator.Dot)) {

        }

        if (b.tryConsume(Punctuator.PointerDereference)) {

        }

        if (b.tryConsume(Punctuator.PlusPlus, Punctuator.MinusMinus)) {

        }

        return lhs;
    }

    private Expression parsePrimary() {
        Expression lhs = getIntExpression();

        return lhs;
    }

    public IntExpression getIntExpression() {
        Token t = b.consume();

        if (t instanceof IntegerToken) {
            IntegerToken intToken = (IntegerToken) t;

            return new IntExpression(intToken);
        }

        throw new RuntimeException("Expected int token, got: " + t);
    }
}
