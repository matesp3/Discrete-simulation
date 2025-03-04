package mpoljak.dsim.assignment_01.generators;

import mpoljak.dsim.common.Generator;

import java.util.Random;

public class ContinuousUniformRnd extends Generator {
    private final Random rand;
    private final double min;
    private final double max;
    /**
     * Generator of values from continuous interval <<code>minValue</code>,<code>maxValue</code>>.
     * @param seedGen generator that is used to initialize all instance's inner generators with 'proper' seed
     * @param minValue minimal value of interval from which are values generated. This value is included.
     * @param maxValue maximal value of interval. This value is excluded.
     */
    public ContinuousUniformRnd(Random seedGen, double minValue, double maxValue) {
        super(seedGen);
        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue > maxValue");

        this.rand = new Random(seedGen.nextLong());
        this.min = minValue;
        this.max = maxValue;
    }

    @Override
    public double sample() {
        return this.rand.nextDouble() *( this.max - this.min) + this.min;
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -

    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        final int n = 50_000;
        double min = 5;
        double max = 10;
        ContinuousUniformRnd rnd = new ContinuousUniformRnd(new Random(), min, max);
        for (int i = 0; i < n; i++) {
            System.out.println(rnd.sample());
        }
    }
}
