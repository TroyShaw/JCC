package compiler.language.statements;

/**
 * Created by troy on 14/11/14.
 */
public class DefaultStatement extends Statement {

    private Statement body;

    public DefaultStatement(Statement body) {
        this.body = body;
    }
}
