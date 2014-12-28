package compiler.parser;

import compiler.language.declarations.Declaration;

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
        return null;
    }
}
