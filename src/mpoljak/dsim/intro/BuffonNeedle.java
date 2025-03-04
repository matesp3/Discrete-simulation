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

    public BuffonNeedle(Random seedGenerator, long replications, double needleLength, double linesDist) {
        super(seedGenerator, replications);
        this.d = linesDist;
        this.l = needleLength;
        this.genNeedle = new Random(seedGenerator.nextLong());
        this.genAngle = new Random(seedGenerator.nextLong());
    }

    @Override
    protected void experiment() {
        double y = genNeedle.nextDouble() * this.d;
        double alfaRad = genAngle.nextDouble() * Math.PI;
            /* here is the paradox of PI. We need to use radians, while we know that 180deg = 3.14 rad. Without PI, it
               is not possible to calculate PI.
            This is paradox, which only occurs in programming. Buffon didn't need to generate angle. */
//        return (y + this.l * Math.sin(alfaRad)) >= this.d ? 1.0 : 0.0; // Math.sin works with radians, not degrees
    }

    @Override
    protected void beforeSimulation() {

    }

    @Override
    protected void afterSimulation() {

    }

    @Override
    protected void beforeExperiment() {

    }

    @Override
    protected void afterExperiment() {

    }
}