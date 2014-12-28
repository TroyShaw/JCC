package compiler.parser;

import compiler.language.expressions.Expression;
import compiler.language.statements.*;
import compiler.lexer.Keyword;
import compiler.lexer.Punctuator;
import compiler.lexer.token.Token;

import java.util.List;

/**
 * Created by troy on 14/11/14.
 */
public class StatementParser {

    private Parser parentParser;
    private ParserBuffer b;

    public StatementParser(Parser parentParser, ParserBuffer b) {
        this.parentParser = parentParser;
        this.b = b;
    }

    public Statement parseStatement() {

        //labeled statements
        //TODO identifier labels
        if (b.matches(Keyword.Case)) parseCaseStatement();
        if (b.matches(Keyword.Default)) parseDefaultStatement();

        //jump statements
        if (b.matches(Keyword.Goto)) parseGotoStatement();
        if (b.matches(Keyword.Continue)) parseContinue();
        if (b.matches(Keyword.Break)) parseBreak();
        if (b.matches(Keyword.Return)) parseReturn();

        //selection statements
        if (b.matches(Keyword.If)) parseIfStatement();
        if (b.matches(Keyword.Switch)) parseSwitchStatement();

        //iteration statements
        if (b.matches(Keyword.Do)) parseDoStatement();
        if (b.matches(Keyword.While)) parseWhileStatement();
        if (b.matches(Keyword.For)) parseForStatement();

        //compound statement
        if (b.matches(Punctuator.LeftCurlyBracket)) parseCompoundStatement();

        //expression and null statement

        return null;
    }

    private Expression parseExpression() {
        return parentParser.parseExpression();
    }

    private Statement parseCaseStatement() {
        b.consume(Keyword.Case);

        Expression constantExpr = parseExpression();

        b.consume(Punctuator.Colon);

        Statement caseStatement = parseStatement();

        return new CaseStatement(constantExpr, caseStatement);
    }

    private Statement parseDefaultStatement() {
        b.consume(Keyword.Default);

        b.consume(Punctuator.Colon);

        Statement caseStatement = parseStatement();

        return new DefaultStatement(caseStatement);
    }

    private Statement parseGotoStatement() {
        b.consume(Keyword.Goto);

        //TODO: check next token is an indentifier
        String gotoLabel = null;

        return new GotoStatement(gotoLabel);
    }

    private Statement parseContinue() {
        b.consume(Keyword.Continue);

        return new ContinueStatement();
    }

    private Statement parseBreak() {
        b.consume(Keyword.Break);

        return new BreakStatement();
    }

    private Statement parseReturn() {
        b.consume(Keyword.Return);

        //TODO: get optional expression
        Expression returnExpr = null;

        b.consume(Punctuator.SemiColon);

        return new ReturnStatement(returnExpr);
    }

    private Statement parseWhileStatement() {
        b.consume(Keyword.While);
        b.consume(Punctuator.LeftParenthesis);
        Expression condition = parseExpression();
        b.consume(Punctuator.RightParenthesis);
        Statement body = parseStatement();

        return new WhileStatement(condition, body);
    }

    private Statement parseDoStatement() {
        b.consume(Keyword.Do);
        Statement statement = parseStatement();
        b.consume(Keyword.While);
        b.consume(Punctuator.LeftParenthesis);
        Expression condition = parseExpression();
        b.consume(Punctuator.RightParenthesis);
        b.consume(Punctuator.SemiColon);

        return new DoStatement(condition, statement);
    }

    private Statement parseIfStatement() {
        Expression condition = null;
        Statement ifStatement = null;
        Statement elseStatement = null;

        b.consume(Keyword.If);
        b.consume(Punctuator.LeftParenthesis);
        condition = parseExpression();
        b.consume(Punctuator.RightParenthesis);
        ifStatement = parseStatement();

        if (b.tryConsume(Keyword.Else)) {
            elseStatement = parseStatement();
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement parseSwitchStatement() {
        b.consume(Keyword.Switch);
        b.consume(Punctuator.LeftParenthesis);
        Expression switchExpr = parseExpression();
        b.consume(Punctuator.RightParenthesis);
        Statement statement = parseStatement();

        return new SwitchStatement(switchExpr, statement);
    }

    private Statement parseForStatement() {
        //TODO: implement this properly

        return new ForStatement();
    }

    private Statement parseCompoundStatement() {
        return null;
    }
}
