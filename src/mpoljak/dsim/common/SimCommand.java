package mpoljak.dsim.common;

public abstract class SimCommand {
    public enum SimCommandType {
        BEFORE_SIM, BEFORE_EXP, AFTER_EXP, AFTER_SIM, CUSTOM
    }
    private final SimCommandType type;

    public SimCommand(SimCommandType commandType) {
        type = commandType;
    }

    public abstract void invoke();

    public SimCommandType getCommandType() {
        return this.type;
    }
}
