package mpoljak.dsim.assignment_01.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;

import java.util.Random;

public abstract class EmpiricalRnd extends Generator {
    private final Random rndIntervalPicker;
    private final Random[] valGen;
    private final double[] probs;
    private final double[] low;
    private final double[] upper;

    /**
     * Generator for empirical probability. Suppose, generator consists of <code>n</code> intervals and
     * therefore <code>n</code> probabilities. These <code>n</code> intervals are characterized by their lower bounds,
     * upper bounds and their probabilities. I-th element of <code>lowerBounds</code> array is lower bound that
     * corresponds to i-th element of <code>upperBounds</code> array and i-th probability of
     * <code>intervalProbabilities</code> array.
     * @param seedGen generator that is used to initialize all instance's inner generators with 'proper' seed
     * @param lowerBounds minimal interval values. They are INCLUDED.
     * @param upperBounds maximum interval values. They are EXCLUDED.
     * @param intervalProbabilities i-th element is probability to generate values from i-th interval
     */
    public EmpiricalRnd(Random seedGen, double[] lowerBounds, double[] upperBounds, double[] intervalProbabilities) {
        super(seedGen);
        if (lowerBounds == null || upperBounds == null || intervalProbabilities == null)
            throw new IllegalArgumentException("Arrays cannot be null");
        if (lowerBounds.length != upperBounds.length && intervalProbabilities.length != lowerBounds.length)
            throw new IllegalArgumentException("Arrays must have the same length corresponding to the number of intervals");
        double probSum = 0;
        for (int i = 0; i < intervalProbabilities.length; i++) {
            probSum += intervalProbabilities[i];
        }
        if (DoubleComp.compare(probSum, 1.0) != 0)
            throw new IllegalArgumentException("Sum of probabilities must be 1.0");

        this.probs = new double[lowerBounds.length];
        System.arraycopy(intervalProbabilities, 0, this.probs, 0, lowerBounds.length);
        this.low = new double[lowerBounds.length];
        System.arraycopy(lowerBounds, 0, this.low, 0, lowerBounds.length);
        this.upper = new double[upperBounds.length];
        System.arraycopy(upperBounds, 0, this.upper, 0, lowerBounds.length);

        // generators initialization
        this.rndIntervalPicker = new Random(seedGen.nextLong());
        this.valGen = new Random[lowerBounds.length];
        for (int i = 0; i < this.valGen.length; i++) {
            this.valGen[i] = new Random(seedGen.nextLong());
        }
    }

    @Override
    public double sample() {
        final double val = rndIntervalPicker.nextDouble();
        int i = 0;
        double cumulated = this.probs[i];
        while (DoubleComp.compare(cumulated,val) < 1) {  // while (cumulated <= val)
            i++;
            /* due to epsilon, it can happen, that program comes here even if: cumulated = 1,000000 ; val = 0,999982
                This situation, it shouldn't be here inside the loop, so we have to verify it with index condition
            */
            if (i == this.valGen.length) {  // making sure that we don't run out of index
                i--;    // last interval will be used
                break;
            }
            cumulated += this.probs[i];
        }
        return this.generateSample(low[i], upper[i], valGen[i]);
    }

    /**
     * @param min lower bound of interval. This value is INCLUDED.
     * @param max upper bound of interval. This value is EXCLUDED.
     * @param valGen generator to be used to get sample
     * @return generated sample from interval <<code>min</code>,<code>max</code>) by generator <code>valGen</code>
     */
    protected abstract double generateSample(double min, double max, Random valGen);
}
