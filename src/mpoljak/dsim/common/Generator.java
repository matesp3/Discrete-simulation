package mpoljak.dsim.common;

import java.util.Random;

/**
 * Common class for all generators, which can be empirical or theoretical.
 */
public abstract class Generator {
    /**
     *
     * @param seedGen generator that is used to initialize instance's all inner generators with 'proper' seed
     */
    public Generator(Random seedGen) {
        if (seedGen == null)
            throw new NullPointerException("Seed generator not provided!");
    }

    /**
     * Generates next value.
     * @return generated value regarding generator's context
     */
    public abstract double sample();
}
