package compiler.lexer.token.cString;

/**
 * Class encapsulates the details of a string of characters in either a
 * string or char constant in C source code.
 *
 * Both strings and character
 */
public class CCharSequence {

    private StringBuilder builder = new StringBuilder();

    private boolean isWide;

    public CCharSequence(boolean isWide) {
        this.isWide = isWide;
    }

    public void addChar(char c) {
        builder.append(c);
    }

    public void addString(String string) {
        builder.append(string);
    }

    public void addHexString(String hexDigits) {

    }

    public void addUCN() {

    }

    public String getString() {
        return builder.toString();
    }

    public int length() {
        return builder.length();
    }

    public boolean isWide() {
        return isWide;
    }
}
