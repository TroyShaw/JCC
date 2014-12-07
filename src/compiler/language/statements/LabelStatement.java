package compiler.language.statements;

/**
 * Created by troy on 14/11/14.
 */
public class LabelStatement extends Statement {

    private String labelName;
    private Statement statement;

    public LabelStatement(String labelName, Statement statement) {
        this.labelName = labelName;
        this.statement = statement;
    }

    public String getLabelName() {
        return labelName;
    }

    public Statement getStatement() {
        return statement;
    }
}
