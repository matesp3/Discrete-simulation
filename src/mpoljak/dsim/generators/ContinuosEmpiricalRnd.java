package mpoljak.dsim.generators;

import java.util.Random;

public class ContinuosEmpiricalRnd extends EmpiricalRnd {
    /**
     * Generator for empirical probability. Suppose, generator consists of <code>n</code> intervals and
     * therefore <code>n</code> probabilities. These <code>n</code> intervals are characterized by their lower bounds,
     * upper bounds and their probabilities. I-th element of <code>lowerBounds</code> array is lower bound that
     * corresponds to i-th element of <code>upperBounds</code> array and i-th probability of
     * <code>intervalProbabilities</code> array.
     *
     * @param lowerBounds           minimal interval values. They are INCLUDED.
     * @param upperBounds           maximum interval values. They are EXCLUDED.
     * @param intervalProbabilities i-th element is probability to generate values from i-th interval
     */
    public ContinuosEmpiricalRnd(double[] lowerBounds, double[] upperBounds, double[] intervalProbabilities) {
        super(lowerBounds, upperBounds, intervalProbabilities);
    }

    @Override
    protected double generateSample(double min, double max, Random valGen) {
        return valGen.nextDouble()*(max-min) + min;
    }
    //  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        int n = 50_000;
        // generate n values
        ContinuosEmpiricalRnd rnd = new ContinuosEmpiricalRnd(
                new double[]{5, 10, 50, 70, 80},      // supplier 1 - until week 15
                new double[]{10, 50, 70, 80, 95},
                new double[]{0.4, 0.3, 0.2, 0.06, 0.04}
//                new double[]{5, 10, 50, 70, 80},        // supplier 1 - from week 16
//                new double[]{10, 50, 70, 80, 95},
//                new double[]{0.2, 0.4, 0.3, 0.06, 0.04}
        );
        for (int i = 0; i < n; i++)
            System.out.println(rnd.sample());
    }
}
