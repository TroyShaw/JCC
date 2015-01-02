package compiler.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by troy on 30/12/14.
 */
public class ExternalDefinitionParser {

    private final DeclarationParser declarationParser;

    public ExternalDefinitionParser(DeclarationParser declarationParser) {
        this.declarationParser = declarationParser;
    }

    /**
     * Parses eternal definitions. This is essentially one or more of:
     * - function definitions
     * - declarations
     *
     * @return
     */
    public Object parseExternalDefinitionParser() {
        List<Object> objects = new ArrayList<Object>();

        //function definition

        //declaration
        return null;
    }
}
