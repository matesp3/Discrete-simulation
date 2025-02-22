package mpoljak.dsim.common;

/**
 * Monte Carlo simulation core class.
 */
public abstract class MCSimCore {

    /**
     * This is MONTE CARLO.
     * Executes <code>repCount</code> same experiments including random variables and calculates probability of event.
     * @param repCount amount of executed experiments
     * @return calculated probability for this event.
     */
    public double run(long repCount) { // template method
        double cumRes = 0;
        for (long i = 0; i < repCount; i++) {
            cumRes += this.experiment();
        }
        return cumRes / repCount;
    }

    /**
     * Executes experiment specific for each type.
     * @return result of experiment
     */
    protected abstract double experiment();

}
