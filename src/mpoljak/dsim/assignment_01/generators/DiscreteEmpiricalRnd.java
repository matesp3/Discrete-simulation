package mpoljak.dsim.assignment_01.generators;

import java.util.Random;

public class DiscreteEmpiricalRnd extends EmpiricalRnd {
    /**
     * Generator for empirical probability. Suppose, generator consists of <code>n</code> intervals and
     * therefore <code>n</code> probabilities. These <code>n</code> intervals are characterized by their lower bounds,
     * upper bounds and their probabilities. I-th element of <code>lowerBounds</code> array is lower bound that
     * corresponds to i-th element of <code>upperBounds</code> array and i-th probability of
     * <code>intervalProbabilities</code> array.
     *
     * @param seedGen               generator that is used to initialize all instance's inner generators with 'proper' seed
     * @param lowerBounds           minimal interval values. They are INCLUDED.
     * @param upperBounds           maximum interval values. They are EXCLUDED.
     * @param intervalProbabilities i-th element is probability to generate values from i-th interval
     */
    public DiscreteEmpiricalRnd(Random seedGen, double[] lowerBounds, double[] upperBounds, double[] intervalProbabilities) {
        super(seedGen, lowerBounds, upperBounds, intervalProbabilities);
        for (int i = 0; i < lowerBounds.length; i++) {
            if (lowerBounds[i] != Math.floor(lowerBounds[i]) || upperBounds[i] != Math.ceil(upperBounds[i]))
                throw new IllegalArgumentException("Found value in lower or uppers bound's array, that is not discrete");
        }

    }

    @Override
    protected double generateSample(double min, double max, Random valGen) {
        return valGen.nextInt((int)max-(int)min) + min;
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
                new double[]{30, 60, 100.0, 140},
                new double[]{60, 100, 140, 160},
                new double[]{0.2, 0.4, 0.3, 0.1}
        );
        for (int i = 0; i < n; i++)
            System.out.println(rnd.sample());
    }
}
