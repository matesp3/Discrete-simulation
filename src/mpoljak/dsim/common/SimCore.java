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

    public synchronized boolean isPaused() {
        return this.paused;
    }

    public synchronized void setPaused(boolean pause) { // monitor is reference of this class instance
        System.out.println(Thread.currentThread().getName() + ": value="+pause);
        this.paused = pause;
        if (!this.paused) {
            this.notify();
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
            int sample = 0;
//            while (! this.ended) { // todo priority queue.isNotEmpty() & timeNotAtEnd()
                synchronized (this) {
                    while (this.paused) { // while because of spurious wakeup
                        System.out.println(Thread.currentThread().getName() + ": going to sleep...");
                        this.wait(); // going to sleep

                        // after notification, thread is woken up and when it gets monitor, it continues here
                        System.out.println(Thread.currentThread().getName() + ": resumed!");
                    }
                }
//                Thread.sleep(250);
                this.beforeExperiment();    // hook - before rep
                this.experiment();          // main stuff
                // todo priorityQueue.poll().execute(); // event.experiment()
                this.afterExperiment();     // hook - after rep

                this.notifyDelegates(sample++); // todo this will go to the child's execute method
//            }
            this.currentRep++;
        }
        this.afterSimulation();         // hook - after sim

        System.out.println(Thread.currentThread().getName() + ": simulation ended successfully.");
    }

    protected abstract void experiment();

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

    private void executeCommandsOfType(SimCommand.SimCommandType type) {
        for (SimCommand command : this.commands) {
            if (command.getCommandType().compareTo(type) == 0)
                command.invoke();
        }
    }

    private void notifyDelegates(int val) {
        for (ISimDelegate d : this.delegates) {
            d.refresh(val);
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
