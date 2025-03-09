package mpoljak.dsim.assignment_01.logic.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.SeedGen;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Random;

public class DiscreteUniformRnd extends Generator {
    private final Random rand;
    private final int min;
    private final int max;
    /**
     * Generator of discrete values (integers) from interval <<code>minValue</code>,<code>maxValue</code>>.
     * @param minValue minimal value of interval from which are values generated. This value is included.
     * @param maxValue maximal value of interval. This value is included also.
     */
    public DiscreteUniformRnd(int minValue, int maxValue) {
        if (minValue  < 0)
            throw new IllegalArgumentException("minValue cannot be negative");
        if (maxValue <= 0)
            throw new IllegalArgumentException("maxValue must be greater than 0");
        if (minValue >= maxValue)
            throw new IllegalArgumentException("maxValue must be greater than minValue");

        this.rand = new Random(SeedGen.getInstance().nextSeed());
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
        DiscreteUniformRnd rnd = new DiscreteUniformRnd(min, max);

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
        DiscreteUniformRnd rnd = new DiscreteUniformRnd(min, max);
        for (int i = 0; i < n; i++) {
            System.out.println(rnd.sample());
        }
    }
}
