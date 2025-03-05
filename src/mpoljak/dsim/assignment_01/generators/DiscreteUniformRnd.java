package mpoljak.dsim.assignment_01.generators;

import mpoljak.dsim.common.Generator;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Random;

public class DiscreteUniformRnd extends Generator {
    private final Random rand;
    private final int min;
    private final int max;
    /**
     * Generator of discrete values (integers) from interval <<code>minValue</code>,<code>maxValue</code>>.
     * @param seedGen generator that is used to initialize all instance's inner generators with 'proper' seed
     * @param minValue minimal value of interval from which are values generated. This value is included.
     * @param maxValue maximal value of interval. This value is included also.
     */
    public DiscreteUniformRnd(Random seedGen, int minValue, int maxValue) {
        super(seedGen);
        if (minValue > maxValue)
            throw new IllegalArgumentException("minVal > maxVal");

        this.rand = new Random(seedGen.nextLong());
        this.min = minValue;
        this.max = maxValue + 1;
    }

    @Override
    public double sample() {
        return this.rand.nextInt(this.max - this.min) + this.min;
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    // test of generated values
    public static void main(String[] args) throws UnexpectedException {
//        printForTest(); // ok

        final int reps = 100_000;
        int min = 1;
        int max = 7;
        ArrayList<Integer> uniqueGenVals = new ArrayList<>(max-min+1);
        DiscreteUniformRnd rnd = new DiscreteUniformRnd(new Random(), min, max);

        for (int i = 0; i < reps; i++) {
            int val = (int) rnd.sample();
            boolean unique = true;
            for (int j = 0; j < uniqueGenVals.size(); j++) {
                if (val == uniqueGenVals.get(j)) {
                    unique = false;
                    break; // found
                }
            }
            if (unique)
                uniqueGenVals.add(val);
        }
        System.out.println("Generated values: "+uniqueGenVals);
        // size test
        if (uniqueGenVals.size() != max-min+1)
            throw new UnexpectedException(String.format("Number of unique generated values (%d) is different than" +
                    " expected (%d)", uniqueGenVals.size(), max-min+1));
        // values presence test
        for (int wanted = min; wanted < max+1; wanted++) {
            boolean found = false;
            for (int j = 0; j < uniqueGenVals.size(); j++) {
                if (wanted == uniqueGenVals.get(j)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                throw new UnexpectedException(String.format("Value %d not found", wanted));
        }
        System.out.println("Ok.. tests passed.");
    }

    private static void printForTest() {
        final int n = 50_000;
        int min = 50;
        int max = 100;
        DiscreteUniformRnd rnd = new DiscreteUniformRnd(new Random(), min, max);
        for (int i = 0; i < n; i++) {
            System.out.println(rnd.sample());
        }
    }
}
