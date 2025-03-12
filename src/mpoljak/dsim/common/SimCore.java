package mpoljak.dsim.common;

import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

import java.util.ArrayList;

public abstract class SimCore {
    protected final SimulationTask simTask;
    private final ArrayList<SimCommand> commands;
    private final long repCount;
    private long currentRep;

    public SimCore(long replicationsCount, SimulationTask simTask) {
        this.simTask = simTask;
        this.currentRep = 0;
        this.repCount = replicationsCount;
        this.commands = new ArrayList<>();
    }

    /**
     * @return number of current replication
     */
    public long getCurrentReplication() {
        return this.currentRep;
    }

    /**
     * @return number of replication that are executed within one simulation.
     */
    public long getRepCount() {
        return this.repCount;
    }

    /**
     * Stores <code>command</code>, which will be invoked when it's time (defined by
     * <code>SimCommand.getCommandType()</code>) will come.
     * @param command to be executed in time specified by type of command <code>SimCommand.SimCommandType</code>.
     */
    public void storeCommand(SimCommand command) {
        if (command == null)
            return;
        for (SimCommand simCommand : this.commands) {
            if (simCommand == command)
                return;
        }
        this.commands.add(command);
    }

    /**
     * Launches and executes whole simulation.
     */
    public final void simulate() { // TEMPLATE METHOD
        this.currentRep = 0;            // reset
        this.beforeSimulation();        // hook - before sim
        for (int i = 0; i < this.repCount; i++) {
//            v--- concurrency work
            if (this.simTask != null && this.simTask.isCancelled()) {
                System.out.println("\n          !!! SIMULATION CANCELLED !!!");
                break;
            }
//            ^--- concurrency work
            this.currentRep++;
            this.beforeExperiment();    // hook - before rep
            this.experiment();          // main stuff
            this.afterExperiment();     // hook - after rep
        }
        this.afterSimulation();         // hook - after sim
    }

    protected abstract void experiment();
    protected void beforeSimulation() {
        this.executeCommandsOfType(SimCommand.SimCommandType.BEFORE_SIM);
    }
    protected void afterSimulation() {
        this.executeCommandsOfType(SimCommand.SimCommandType.AFTER_SIM);
    }
    protected void beforeExperiment() {
        this.executeCommandsOfType(SimCommand.SimCommandType.BEFORE_EXP);
    }
    protected void afterExperiment() {
        this.executeCommandsOfType(SimCommand.SimCommandType.AFTER_EXP);
    }

    private void executeCommandsOfType(SimCommand.SimCommandType type) {
        for (SimCommand command : this.commands) {
            if (command.getCommandType().compareTo(type) == 0)
                command.invoke();
        }
    }
}
