package mpoljak.dsim.assignment_01.controllers;

import mpoljak.dsim.assignment_01.gui.SimVisualization;
import mpoljak.dsim.assignment_01.logic.experiments.*;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;
import mpoljak.dsim.utils.DoubleComp;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;

public class SimController {
    private static final String[] STRATEGIES = {"strategy A", "strategy B", "strategy C", "strategy D",
            "Matej's strategy 1", "custom strategy"};
    private static final int DEFAULT_STRATEGY = 0;
    /**
     * @return IDs of all existing strategies, that can be used
     */
    public static String[] getStrategies() {
        String[] strategs = new String[STRATEGIES.length];
        System.arraycopy(STRATEGIES, 0, strategs, 0, STRATEGIES.length);
        return strategs;
    }

    public static String getCustomStrategyID() {
        return "custom strategy";
    }

    public static String getDefaultStrategyID() {
        return STRATEGIES[DEFAULT_STRATEGY];
    }

    //  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    private final ContinuosUniformRnd rndConfSupplier1A;
    private final ContinuosUniformRnd rndConfSupplier1B;
    private final ContinuosEmpiricalRnd rndConfSupplier2A;
    private final ContinuosEmpiricalRnd rndConfSupplier2B;
    private final Supplier supplier1;
    private final Supplier supplier2;

    private SimulationTask simTask;
    private SimVisualization gui;
    private XYSeriesCollection xyDatasetAll;
    private XYSeriesCollection xyDataset1Rep;
    private File customStrategyFile;
    private int strategy;
    private boolean running; // created because it didn't work with isDone(), when it didn't even start/

    public SimController(SimVisualization gui) {
        //      --- strategies vars
        this.rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        this.rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        this.supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
        this.rndConfSupplier2A = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80}, new double[] {10, 50, 70, 80, 95}, new double[]{0.4, 0.3, 0.2, 0.06, 0.04});
        this.rndConfSupplier2B = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80}, new double[] {10, 50, 70, 80, 95}, new double[] {0.2, 0.4, 0.3, 0.06, 0.04});
        this.supplier2 = new Supplier(16, rndConfSupplier2A, rndConfSupplier2B);
        //      --- task vars
        this.customStrategyFile = null;
        this.strategy = DEFAULT_STRATEGY;
        this.running = false;
        this.gui = gui;
        this.simTask = new SimulationTask(this.assembleStrategy(STRATEGIES[this.strategy]), this);

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

    /**
     *
     * @param customFile file from which will be loaded custom strategy when simulation will be launched
     */
    public void setFileCustomStrategy(File customFile) {
        this.customStrategyFile = customFile;
    }

    public void startSimulation(int replications, double percentageOmission) {
        if (this.simTask.isDone())
            this.simTask = this.simTask.cloneInstance();
        this.simTask.setStrategy(this.assembleStrategy(STRATEGIES[this.strategy]));
        int threshold =  (int)( replications * (percentageOmission/100.0));
        this.simTask.setOmittedReps(threshold);
        int nthVal = DoubleComp.compare((replications-threshold)/1000.0, 1.0) == -1 ? 1 : (replications-threshold)/1000; // (replications/1000.0 < 1.0)
        this.simTask.setNthVal(nthVal);
        this.simTask.setReplications(replications);
//        System.out.printf("reps=%d  omitted=%d  nthVal=%d", replications, threshold, nthVal);

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
        switch (strategyID) {
            case "strategy A":
                return new SingleSupply(this.supplier1, 100, 200, 150);
            case "strategy B":
                return new SingleSupply(this.supplier2, 100, 200, 150);
            case "strategy C":
                return new AlternatingSupply(this.supplier1, this.supplier2, 100, 200, 150);
            case "strategy D":
                return new AlternatingSupply(this.supplier2, this.supplier1, 100, 200, 150);
            case "Matej's strategy 1":
                // todo Matej's strategy
            case "custom strategy":
                return new CustomSupply(this.customStrategyFile, this.supplier1, this.supplier2);
            default:
                return null;
        }
    }
}
