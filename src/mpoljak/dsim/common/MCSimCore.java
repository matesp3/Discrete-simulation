package mpoljak.dsim.common;

import mpoljak.dsim.assignment_01.Main;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;
import mpoljak.dsim.utils.DoubleComp;

/**
 * Monte Carlo simulation core class.
 */
public abstract class MCSimCore extends SimCore {
    private double accumulatedVal;

    public MCSimCore(long repCount, SimulationTask simTask) {
        super(repCount, simTask);
        this.resetAccumulation();
    }

    /**
     * @return result of the Monte Carlo simulation, which is a probability of the observed event by this instance.
     */
    public double getResult() {
        return this.accumulatedVal / this.getCurrentReplication();
    }

    /**
     * Adds <code>value</code> to cumulated value of all executed experiments so far.
     * @param value result of currently executed experiment
     */
    protected final void cumulate(double value) {
        if (DoubleComp.compare(value, 0.0) > -1) { // (value >= 0)
            this.accumulatedVal += value;
        }
    }

    @Override
    protected void beforeSimulation() {
        this.resetAccumulation();
        super.beforeSimulation();
    }

    /**
     * Cumulated value of all executed experiments so far is reset to 0.
     */
    private void resetAccumulation() {
        this.accumulatedVal = 0;
    }
}
