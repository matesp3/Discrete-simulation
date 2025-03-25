package mpoljak.dsim.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.SeedGen;

import java.util.Random;

public class ExponentialRnd extends Generator {
    private final Random rnd;
    private final double rate; // lambda -> expected amount of events in one time unit -> 1/rate = time between events

    public ExponentialRnd(double rate) {
        this.rnd = new Random(SeedGen.getInstance().nextSeed());
        this.rate = rate;
    }

    @Override
    public double sample() { // https://en.wikipedia.org/wiki/Exponential_distribution
        return -Math.log(rnd.nextDouble()) / this.rate; // -ln(U)/lambda
    }

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        int n = 5000;
        // generate n values
        ExponentialRnd rnd = new ExponentialRnd(2);
        for (int i = 0; i < n; i++)
            System.out.println(rnd.sample());
    }
}
