package mpoljak.dsim.common;

import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

public abstract class SimCore {
    protected final SimulationTask simTask;
    private final long repCount;
    private long currentRep;

    public SimCore(long replicationsCount, SimulationTask simTask) {
        this.simTask = simTask;
        this.currentRep = 0;
        this.repCount = replicationsCount;
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
    protected abstract void beforeSimulation();
    protected abstract void afterSimulation();
    protected abstract void beforeExperiment();
    protected abstract void afterExperiment();
}
