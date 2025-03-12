package mpoljak.dsim.assignment_01.logic.simulations;

public interface IMcExpMetaResultsCollector {
    /**
     * Collects pair (<code>x</code>,<code>y</code>) into internal collector.
     * @param x parameter value
     * @param y dependent value
     */
    public void collectResult(double x, double y);
}
