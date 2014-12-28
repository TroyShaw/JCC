package compiler.parser;

import compiler.lexer.Keyword;
import compiler.lexer.Punctuator;
import compiler.lexer.token.KeywordToken;
import compiler.lexer.token.PunctuatorToken;
import compiler.lexer.token.Token;

import java.util.List;

/**
 * Created by troy on 21/12/14.
 */
public class ParserBuffer {

    private List<Token> tokens;
    private int pos;

    private Token lastMatched;
    private Punctuator lastMatchedPunctuator;

    public ParserBuffer(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean hasToken() {
        return pos < tokens.size();
    }


    public Token peekToken() {
        return tokens.get(pos);
    }

    public Token consume() {
        return tokens.get(pos++);
    }

    public void consume(Keyword keyword) {
        if (!matches(keyword)) throw new Error("error matching keyword: " + keyword);

        pos++;
    }

    public void consume(Punctuator punctuator) {
        if (!matches(punctuator)) throw new Error("error matching punctuator: " + punctuator);


        pos++;
    }

    public boolean tryConsume(Keyword keyword) {
        if (!matches(keyword)) return false;

        consume(keyword);

        return true;
    }

    public boolean tryConsume(Keyword ... args) {
        for (Keyword keyword : args) {
            if (matches(keyword)) {
                consume(keyword);
                return true;
            }
        }

        return false;
    }

    public boolean tryConsume(Punctuator ... args) {
        for (Punctuator punctuator : args) {
            if (matches(punctuator)) {
                consume(punctuator);
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the last token that was matched against.
     *
     * @return the last matched token
     */
    public Token lastMatched() {
        return lastMatched;
    }

    public Punctuator lastMatchedPunctuator() {
        return lastMatchedPunctuator;
    }

    public boolean matches(Keyword keyword) {
        if (!hasToken()) return false;

        Token next = peekToken();

        if (next instanceof KeywordToken) {
            KeywordToken token = (KeywordToken) next;

            if (token.getKeyword() != keyword) {
                return false;
            } else {
                lastMatched = next;
                return true;
            }
        }

        return false;
    }

    public boolean matches(Punctuator punctuator) {
        if (!hasToken()) return false;

        Token next = peekToken();

        if (next instanceof PunctuatorToken) {
            PunctuatorToken token = (PunctuatorToken) next;

            if (token.getPunctuator() != punctuator) {
                return false;
            } else {
                lastMatched = next;
                lastMatchedPunctuator = token.getPunctuator();
                return true;
            }
        }

        return false;
    }
}
