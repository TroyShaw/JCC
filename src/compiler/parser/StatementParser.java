package compiler.parser;

import compiler.language.expressions.Expression;
import compiler.language.statements.*;
import compiler.lexer.Keyword;
import compiler.lexer.Punctuator;

/**
 * Created by troy on 14/11/14.
 */
public class StatementParser {


    private Statement parseStatement() {

        //labeled statements
        //TODO identifier labels
        if (matches(Keyword.Case)) parseCaseStatement();
        if (matches(Keyword.Default)) parseDefaultStatement();

        //jump statements
        if (matches(Keyword.Goto)) parseGotoStatement();
        if (matches(Keyword.Continue)) parseContinue();
        if (matches(Keyword.Break)) parseBreak();
        if (matches(Keyword.Return)) parseReturn();

        //selection statements
        if (matches(Keyword.If)) parseIfStatement();
        if (matches(Keyword.Switch)) parseSwitchStatement();

        //iteration statements
        if (matches(Keyword.Do)) parseDoStatement();
        if (matches(Keyword.While)) parseWhileStatement();
        if (matches(Keyword.For)) parseForStatement();

        //compound statement
        if (matches(Punctuator.LeftCurlyBracket)) parseCompoundStatement();

        //expression and null statement

        return null;
    }

    private Expression parseExpression() {
        return null;
    }

    private Statement parseCaseStatement() {
        consumeKeyword(Keyword.Case);

        Expression constantExpr = parseExpression();

        consumePunctuator(Punctuator.Colon);

        Statement caseStatement = parseStatement();

        return new CaseStatement(constantExpr, caseStatement);
    }

    private Statement parseDefaultStatement() {
        consumeKeyword(Keyword.Default);

        consumePunctuator(Punctuator.Colon);

        Statement caseStatement = parseStatement();

        return new DefaultStatement(caseStatement);
    }

    private Statement parseGotoStatement() {
        consumeKeyword(Keyword.Goto);

        //TODO: check next token is an indentifier
        String gotoLabel = null;

        return new GotoStatement(gotoLabel);
    }

    private Statement parseContinue() {
        consumeKeyword(Keyword.Continue);

        return new ContinueStatement();
    }

    private Statement parseBreak() {
        consumeKeyword(Keyword.Break);

        return new BreakStatement();
    }

    private Statement parseReturn() {
        consumeKeyword(Keyword.Return);

        //TODO: get optional expression
        Expression returnExpr = null;

        consumePunctuator(Punctuator.SemiColon);

        return new ReturnStatement(returnExpr);
    }

    private Statement parseWhileStatement() {
        consumeKeyword(Keyword.While);
        consumePunctuator(Punctuator.LeftParenthesis);
        Expression condition = parseExpression();
        consumePunctuator(Punctuator.RightParenthesis);
        Statement body = parseStatement();

        return new WhileStatement(condition, body);
    }

    private Statement parseDoStatement() {
        consumeKeyword(Keyword.Do);
        Statement statement = parseStatement();
        consumeKeyword(Keyword.While);
        consumePunctuator(Punctuator.LeftParenthesis);
        Expression condition = parseExpression();
        consumePunctuator(Punctuator.RightParenthesis);
        consumePunctuator(Punctuator.SemiColon);

        return new DoStatement(condition, statement);
    }

    private Statement parseIfStatement() {
        Expression condition = null;
        Statement ifStatement = null;
        Statement elseStatement = null;

        consumeKeyword(Keyword.If);
        consumePunctuator(Punctuator.LeftParenthesis);
        condition = parseExpression();
        consumePunctuator(Punctuator.RightParenthesis);
        ifStatement = parseStatement();

        if (tryConsumeKeyword(Keyword.Else)) {
            elseStatement = parseStatement();
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement parseSwitchStatement() {
        consumeKeyword(Keyword.Switch);
        consumePunctuator(Punctuator.LeftParenthesis);
        Expression switchExpr = parseExpression();
        consumePunctuator(Punctuator.RightParenthesis);
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

    private void consumeKeyword(Keyword keyword) {

    }

    private boolean tryConsumeKeyword(Keyword keyword) {
        return false;
    }

    private void consumePunctuator(Punctuator punctuator) {

    }

    private boolean matches(Keyword keyword) {
        return false;
    }

    private boolean matches(Punctuator punctuator) {
        return false;
    }
}
