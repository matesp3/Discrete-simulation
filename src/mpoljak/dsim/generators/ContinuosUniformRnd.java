package mpoljak.dsim.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.SeedGen;

import java.util.Random;

public class ContinuosUniformRnd extends Generator {
    private final Random rand;
    private final double min;
    private final double max;
    /**
     * Generator of values from continuous interval <<code>minValue</code>,<code>maxValue</code>).
     * @param minValue minimal value of interval from which are values generated. This value is included.
     * @param maxValue maximal value of interval. This value is excluded.
     */
    public ContinuosUniformRnd(double minValue, double maxValue) {
        if (DoubleComp.compare(minValue, 0.0) == -1)   // (minValue < 0)
            throw new IllegalArgumentException("minValue cannot be negative");
        if (DoubleComp.compare(maxValue, 0) < 1)   // (maxValue <= 0)
            throw new IllegalArgumentException("maxValue must be greater than 0");
        if (DoubleComp.compare(minValue, maxValue) > -1)   // (minValue >= maxValue)
            throw new IllegalArgumentException("maxValue must be greater than minValue");

        this.rand = new Random(SeedGen.getInstance().nextSeed());
        this.min = minValue;
        this.max = maxValue;
    }

    @Override
    public double sample() {
        return this.rand.nextDouble() *(this.max - this.min) + this.min;
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        final int n = 50_000;
        double min = 10;
        double max = 70;
        ContinuosUniformRnd rnd = new ContinuosUniformRnd(min, max);
        for (int i = 0; i < n; i++) {
            System.out.println(rnd.sample());
        }
    }
}
