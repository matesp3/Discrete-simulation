package mpoljak.dsim.common;

import java.util.Random;

/**
 * Monte Carlo simulation core class.
 */
public abstract class MCSimCore extends SimCore {
    public MCSimCore(Random seedGenerator, long repCount) {
        super(repCount);
    }

    /**
     * This is MONTE CARLO.
     * Executes <code>repCount</code> same experiments including random variables and calculates probability of event.
     * @param repCount amount of executed experiments
     * @return calculated probability for this event.
     */
    public double run(long repCount) { // template method
        double cumRes = 0;
        for (long i = 0; i < repCount; i++) {
//            cumRes += this.experiment();
        }
        return cumRes / repCount;
    }
}
