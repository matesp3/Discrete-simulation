package mpoljak.dsim.intro;

import mpoljak.dsim.common.MCSimCore;

import java.util.Random;

/**
 * Buffon's needle experiment.
 */
public class BuffonNeedle extends MCSimCore {
    Random genNeedle;
    Random genAngle;
    double d;
    double l;

    public BuffonNeedle(double needleLength, double linesDist) {
        this.d = linesDist;
        this.l = needleLength;
        Random seedGenerator = new Random();
        this.genNeedle = new Random(seedGenerator.nextLong());
        this.genAngle = new Random(seedGenerator.nextLong());
    }

    public BuffonNeedle(double needleLength, double linesDist, long seedForNeedle, long seedForAngle) {
        this.d = linesDist;
        this.l = needleLength;
        this.genNeedle = new Random(seedForNeedle);
        this.genAngle = new Random(seedForAngle);
    }

    @Override
    protected double experiment() {
        double y = genNeedle.nextDouble() * this.d;
        double alfaRad = genAngle.nextDouble() * Math.PI;
            /* here is the paradox of PI. We need to use radians, while we know that 180deg = 3.14 rad. Without PI, it
               is not possible to calculate PI.
            This is paradox, which only occurs in programming. Buffon didn't need to generate angle. */
        return (y + this.l * Math.sin(alfaRad)) >= this.d ? 1.0 : 0.0; // Math.sin works with radians, not degrees
    }
}