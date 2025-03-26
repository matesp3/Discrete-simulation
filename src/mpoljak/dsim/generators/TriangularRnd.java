package mpoljak.dsim.generators;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.SeedGen;

import java.util.Random;

public class TriangularRnd extends Generator {
    private final Random rnd;
    private final double min;
    private final double max;
    private final double mode;
    private final double fmod; // F(mod)

    public TriangularRnd(double min, double max, double mode) {
        if (DoubleComp.compare(min, max) > -1) // min >= max
            throw new IllegalArgumentException("Violated constraint: min < max");
        if (DoubleComp.compare(mode, min) == -1 || DoubleComp.compare(mode, max) == 1) // (mode < min | mode > max)
            throw new IllegalArgumentException("Violated constraint: min <= mode <= max");
        this.rnd = new Random(SeedGen.getInstance().nextSeed());
        this.min = min;
        this.max = max;
        this.mode = mode;
        this.fmod = (mode - min) / (max - min);
    }

    @Override
    public double sample() { // https://en.wikipedia.org/wiki/Triangular_distribution
        double u = rnd.nextDouble();
        if (DoubleComp.compare(u, this.fmod) == -1) // u < F(mod)
            return this.min + Math.sqrt(u * (this.max - this.min) * (this.mode - this.min));
        return this.max - Math.sqrt((1 - u) * (this.max - this.min) * (this.max - this.mode)); // else
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static void main(String[] args) {
        printForTest(); // ok
    }

    private static void printForTest() {
        int n = 10_000;
        // generate n values
        TriangularRnd rnd = new TriangularRnd(15, 50, 45);
        for (int i = 0; i < n; i++)
            System.out.println(rnd.sample());
    }
}
