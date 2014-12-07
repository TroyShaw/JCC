package compiler.language.statements;

/**
 * Created by troy on 14/11/14.
 */
public class GotoStatement extends Statement {

    private String gotoLabel;

    public GotoStatement(String gotoLabel) {
        this.gotoLabel = gotoLabel;
    }

    public String getGotoLabel() {
        return gotoLabel;
    }
}
