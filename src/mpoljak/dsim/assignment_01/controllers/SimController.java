package mpoljak.dsim.assignment_01.controllers;

import mpoljak.dsim.assignment_01.gui.SimVisualization;
import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.event.ActionEvent;

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
    private SimVisualization gui;
    private XYSeriesCollection xyDatasetAll;
    private XYSeriesCollection xyDataset1Rep;
    private int strategy;
    private boolean running; // created because it didn't work with isDone(), when it didn't even start/

    public SimController(SimVisualization gui) {
        ContinuosUniformRnd rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        ContinuosUniformRnd rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        Supplier supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
        SupplyStrategy strategyA = new SingleSupply(supplier1, 100, 200, 150);
//       -   -   -   ^- default strategy  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
        this.gui = gui;
        this.simTask = new SimulationTask(strategyA, this);
        this.strategy = DEFAULT_STRATEGY;
        this.running = false;

        this.xyDatasetAll = new XYSeriesCollection(null);
        this.xyDataset1Rep = new XYSeriesCollection(null);
    }

    public XYSeriesCollection getSimulationDataset() {
        return this.xyDatasetAll;
    }

    public XYSeriesCollection get1RepDataset() {
        return this.xyDataset1Rep;
    }

    public void addValueToSimDataset(long x, double y) {
        this.xyDatasetAll.getSeries(0).add(x, y);
        System.out.printf("x=%d, y=%.2f\n", x, y);
    }

    public void addValueTo1RepDataset(double x, double y) {
        this.xyDataset1Rep.getSeries(0).add(x, y);
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
        System.out.println("execution started");
        XYSeries xySeriesAll = new XYSeries("Averages per replications");
        this.xyDatasetAll.removeAllSeries();
        this.xyDatasetAll.addSeries(xySeriesAll);

        XYSeries xySeries1Rep = new XYSeries("One replication process");
        this.xyDataset1Rep.removeAllSeries();
        this.xyDataset1Rep.addSeries(xySeries1Rep);

        this.simTask.execute();
        this.running = true;
    }

    public void terminateSimulation() {
        if (!this.simTask.isDone()) {
            this.simTask.cancel(true);
            this.running = false;
        }
    }

    public boolean isSimulationRunning() {
        return this.running;
    }

    public void onSimulationEnd() {
        this.running = false;
        this.gui.eventOccurred(SimVisualization.UPDATE_EVENT.SIM_END);
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
