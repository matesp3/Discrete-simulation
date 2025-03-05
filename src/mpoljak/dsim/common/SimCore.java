package mpoljak.dsim.common;

import java.util.Random;

public abstract class SimCore {
    private final long repCount;
    private long currentRep;

    public SimCore(long replicationsCount) {
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

    public final void simulate() { // TEMPLATE METHOD
        this.currentRep = 0;            // reset
        this.beforeSimulation();        // hook - before sim
        for (int i = 0; i < this.repCount; i++) {
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
