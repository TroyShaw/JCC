package compiler.lexer.token;

/**
 * Represents an integer constant token.
 * 
 * @author troy
 */
public class IntegerToken extends LiteralToken {

    private NumericType intType;
    private String suffix;

    public IntegerToken(String intString, String suffix, NumericType intType) {
        super(intString, Literal.Integer);

        this.intType = intType;
        this.suffix = suffix;
    }
}
