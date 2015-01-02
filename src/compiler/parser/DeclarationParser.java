package compiler.parser;

import compiler.language.declarations.Declaration;
import compiler.lexer.Keyword;
import compiler.lexer.Punctuator;

/**
 * Created by troy on 15/11/14.
 */
public class DeclarationParser {

    private Parser parentParser;
    private ParserBuffer b;

    public DeclarationParser(Parser parentParser, ParserBuffer b) {
        this.parentParser = parentParser;
        this.b = b;
    }

    public Declaration parseDeclaration() {
        return null;
    }

    private Declaration parseEnumDeclaration() {
        boolean isStruct;

        if (b.tryConsume(Keyword.Struct)) {
            isStruct = true;
        } else {
            b.consume(Keyword.Union);
            isStruct = false;
        }

        //try consume identifier

        if (b.tryConsume(Punctuator.LeftCurlyBracket)) {

        }

        return null;
    }
}
