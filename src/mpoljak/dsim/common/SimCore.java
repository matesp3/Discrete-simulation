package mpoljak.dsim.common;

public abstract class SimCore {
    private final long repCount;
    private long currentRep;

    public SimCore(long replicationsCount) {
        this.currentRep = 0;
        this.repCount = replicationsCount;
    }

    public final void simulate() { // TEMPLATE METHOD
        this.currentRep = 0; // reset
        this.beforeSimulation();        // hook - before sim
        for (int i = 0; i < this.repCount; i++) {
            this.currentRep++;
            this.beforeExperiment();    // hook - before rep
            this.experiment();          // main stuff
            this.afterExperiment();     // hook - after rep
        }
        this.afterSimulation();         // hook - after sim
    }

    public long getCurrentReplication() {
        return this.currentRep;
    }

    public long getRepCount() {
        return this.repCount;
    }

    protected abstract void experiment();
    protected abstract void beforeSimulation();
    protected abstract void afterSimulation();
    protected abstract void beforeExperiment();
    protected abstract void afterExperiment();
}
