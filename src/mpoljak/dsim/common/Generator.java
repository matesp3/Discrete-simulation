package mpoljak.dsim.common;

/**
 * Common class for all number generators, which can be empirical or theoretical.
 */
public abstract class Generator {

    /**
     * Generates next number value.
     * @return generated number value regarding generator's context
     */
    public abstract double sample();
}
