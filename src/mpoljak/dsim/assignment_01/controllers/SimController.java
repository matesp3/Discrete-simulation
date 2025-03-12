package mpoljak.dsim.assignment_01.controllers;

import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SimController {
    private static final String[] STRATEGIES = {"strategy A", "strategy B", "strategy C", "strategy D",
            "own strategy 1", "custom strategy"};
    private static final int DEFAULT_STRATEGY = 0;

    /**
     * @return IDs of all existing strategies, that can be used
     */
    public static String[] getStrategies() {
        String[] strategs = new String[STRATEGIES.length];
        System.arraycopy(STRATEGIES, 0, strategs, 0, STRATEGIES.length);
        return strategs;
    }

    public static String getDefaultStrategyID() {
        return STRATEGIES[DEFAULT_STRATEGY];
    }
//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    private SimulationTask simTask;
    private XYSeriesCollection xyDataset;
    private int strategy;

    public SimController() {
        ContinuosUniformRnd rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        ContinuosUniformRnd rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        Supplier supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
        SupplyStrategy strategyA = new SingleSupply(supplier1, 100, 200, 150);
//       -   -   -   ^- default strategy  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
        this.simTask = new SimulationTask(strategyA, this);
        this.strategy = DEFAULT_STRATEGY;
        XYSeries xySeries = new XYSeries("sim-dataset");
        this.xyDataset = new XYSeriesCollection(xySeries);
    }

    public XYSeriesCollection getSimulationDataset() {
        return this.xyDataset;
    }

    public XYSeriesCollection get1RepDataset() {
        XYSeries xySeries = new XYSeries("1-rep dataset");
        xySeries.add(1, 5.5);
        xySeries.add(2, 6.5);
        xySeries.add(3, 7.5);
        xySeries.add(4, 4);
        return new XYSeriesCollection(xySeries);
    }

    public void addValueToSimDataset(long x, double y) {
        this.xyDataset.getSeries(0).add(x, y);
    }

    public void addValueTo1RepDataset(int x, double y) {

    }

    /**
     * Sets strategy corresponding to unique strategy's ID specified by <code>strategyID</code> parameter which
     * has to contain one of values retrieved from method <code>SimController.getStrategies()</code>.
     * @param strategyID strategy ID. All possible IDs can be retrieved by calling
     *                      <code>SimController.getStrategies()</code>.
     */
    public void setStrategy(String strategyID) {
        for (int i = 0; i < STRATEGIES.length; i++) {
            if (strategyID.compareTo(STRATEGIES[i])==0) {
                this.strategy = i;
                break;
            }
        }
//        System.out.println("Set Strategy: "+ this.strategy+". ID = "+strategyID);
    }

    public void startSimulation() {
        if (this.simTask.isDone())
            this.simTask = this.simTask.cloneInstance();
//        this.simTask.setStrategy(this.assembleStrategy(STRATEGIES[this.strategy])); // todo <--- doplnit strategie
        this.simTask.execute();
    }

    public void terminateSimulation() {
        if (!this.simTask.isCancelled()) {
            this.simTask.cancel(true);
        }
    }

    public boolean isSimulationRunning() {
        return !this.simTask.isCancelled();
    }

    private SupplyStrategy assembleStrategy(String strategyID) {
        // todo: implement strategies
        switch (strategyID) {
            case "strategy A":
            default:
                return null;
        }
    }
}
