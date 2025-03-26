package mpoljak.dsim.common;

import java.util.ArrayList;
import java.util.List;

public abstract class SimCore {
    private final List<ISimDelegate> delegates;
    private final List<SimCommand> commands;
    private final long repCount;
    private long currentRep;
    private volatile boolean paused;
    private volatile boolean ended;

    public SimCore(long replicationsCount) {
        this.currentRep = 0;
        this.repCount = replicationsCount;
        this.commands = new ArrayList<>();
        this.delegates = new ArrayList<>();
        this.ended = false;
        this.paused = false;
    }

    public boolean isPaused() {
        final boolean p;
        synchronized (this) {
            p = this.paused;
        }
        return p;
    }

    public void setPaused(boolean pause) {
        System.out.println(Thread.currentThread().getName() + ": value="+pause);
        synchronized (this) {
            this.paused = pause;
            if (!this.paused) {
                this.notify();
            }
        }
    }

    /**
     * @return <code>true</code> if simulation is not running anymore (Due to correct shutdown).
     */
    public boolean isEnded() {
        return this.ended;
    }

    /**
     * Cancels (correctly, not violently) simulation if it's running or if it's stopped.
     */
    public void endSimulation() {
        System.out.println(Thread.currentThread().getName() + ": cancelling simulation...");
        this.ended = true;
        synchronized (this) {
            this.paused = false;
            this.notifyAll(); // finish work with all threads that have monitor of this class instance
        }
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
        this.addIfNew(command, this.commands);
    }

    /**
     * Registers objects that want to be notified when some change in simulation's state occurs.
     * @param delegate object to be notified when change occurs.
     */
    public void registerDelegate(ISimDelegate delegate) {
        this.addIfNew(delegate, this.delegates);
    }

    /**
     * Launches and executes whole simulation.
     */
    public final void simulate() throws InterruptedException { // TEMPLATE METHOD
        System.out.println(Thread.currentThread().getName() + ": starting simulation...");

        this.beforeSimulation();        // hook - before sim
        for (int i = 0; (i < this.repCount && !this.ended); i++) {
            this.checkPauseCondition();
            this.beforeExperiment();    // hook - before rep
            this.experiment();          // main stuff
            this.afterExperiment();     // hook - after rep
            this.currentRep++;
        }
        this.afterSimulation();         // hook - after sim

        System.out.println(Thread.currentThread().getName() + ": simulation ended successfully.");
    }

    protected final void checkPauseCondition() throws InterruptedException {
        synchronized (this) {
            while (this.paused) { // while because of spurious wakeup
                System.out.println(Thread.currentThread().getName() + ": going to sleep...");
                this.wait(); // going to sleep
                // after notification, thread is woken up and when it gets monitor, it continues here
                System.out.println(Thread.currentThread().getName() + ": resumed!");
            }
        }
    }

    protected abstract void experiment() throws InterruptedException;
    protected abstract SimResults getLastResults();

    protected void beforeSimulation() {
        this.currentRep = 0;            // reset
        synchronized (this) {
            if (this.ended) {
                this.ended = false;
                this.paused = false;
            }
        }
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

    protected final void notifyDelegates() {
        SimResults r = this.getLastResults();
        for (ISimDelegate d : this.delegates) {
            d.update(r);
        }
    }

    private void executeCommandsOfType(SimCommand.SimCommandType type) {
        for (SimCommand command : this.commands) {
            if (command.getCommandType().compareTo(type) == 0)
                command.invoke();
        }
    }

    private <T> void addIfNew(T adept, List<T> registered) {
        if (adept == null)
            return;
        for (T d : registered) {
            if (d == adept)
                return;
        }
        registered.add(adept);
    }
}
