package mpoljak.dsim.assignment_01.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;

import java.util.Random;

public class DiscreteEmpiricalRnd extends Generator {
    private final Random rndIntervalPicker;
    private final Random[] valGen;
    private final double[] probs;
    private final int[] low;
    private final int[] upper;
    /**
     * Generator for discrete empirical probability. Suppose, generator consists of <code>n</code> intervals and
     * therefore <code>n</code> probabilities. These <code>n</code> intervals are characterized by their lower bounds,
     * upper bounds and their probabilities. I-th element of <code>lowerBounds</code> array is lower bound that
     * corresponds to i-th element of <code>upperBounds</code> array and i-th probability of
     * <code>intervalProbabilities</code> array.
     * @param seedGen generator that is used to initialize all instance's inner generators with 'proper' seed
     * @param lowerBounds minimal interval values. They are INCLUDED.
     * @param upperBounds maximum interval values. They are EXCLUDED.
     * @param intervalProbabilities i-th element is probability to generate values from i-th interval
     */
    public DiscreteEmpiricalRnd(Random seedGen, int[] lowerBounds, int[] upperBounds, double[] intervalProbabilities) {
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
        this.low = new int[lowerBounds.length];
        System.arraycopy(lowerBounds, 0, this.low, 0, lowerBounds.length);
        this.upper = new int[upperBounds.length];
        System.arraycopy(upperBounds, 0, this.upper, 0, lowerBounds.length);

        // generators initialization
        this.rndIntervalPicker = new Random(seedGen.nextLong());
        this.valGen = new Random[lowerBounds.length];
        for (int i = 0; i < this.valGen.length; i++) {
            this.valGen[i] = new Random(seedGen.nextLong());
        }

        // TODO OTESTOVAT GENERATORY EMPIRICKE - V EXCELI VLOZIT 1000 VZORIEK A Z TOHO URCIT ROZDELENIE
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
            try {
                cumulated += this.probs[i];
            } catch (Exception e) {
                System.out.println(String.format("idx[%d] ; cumulated = %f ; val = %f",i, cumulated, val));
                throw e;
            }
        }
        if (i == this.valGen.length)
            throw new IllegalArgumentException(String.format("Index [%d] when maxIndex [%d] | Cumulated: %d.2",i, this.valGen.length-1, cumulated));
        return this.valGen[i].nextInt(upper[i] - low[i]) + low[i];
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        int n = 50_000;
        // generate n values
        DiscreteEmpiricalRnd rnd = new DiscreteEmpiricalRnd(
                new Random(),
                new int[]{30, 60, 100, 140},
                new int[]{60, 100, 140, 160},
                new double[]{0.2, 0.4, 0.3, 0.1}
        );
        for (int i = 0; i < n; i++)
            System.out.println((int)rnd.sample());
    }
}
