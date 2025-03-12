package mpoljak.dsim.assignment_01.logic.tasks;

import mpoljak.dsim.assignment_01.logic.simulations.IMcExpMetaResultsCollector;
import mpoljak.dsim.assignment_01.controllers.SimController;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.simulations.CarComponentsStorage;
import mpoljak.dsim.common.ICloneable;
import mpoljak.dsim.common.SimCommand;

import javax.swing.*;

public class SimulationTask extends SwingWorker<Void, SimulationTask.ReplicationResults> implements ICloneable<SimulationTask> {
    private SupplyStrategy strategy;
    private final SimController controller;

    public SimulationTask(SupplyStrategy defaultStrategy, SimController controller) {
        if (defaultStrategy == null)
            throw new IllegalArgumentException("Default strategy not provided (null)");
        this.strategy = defaultStrategy;
        this.controller = controller;
    }

    public void setStrategy(SupplyStrategy strategy) {
        if (strategy == null)
            return;
        this.strategy = strategy;
    }

    @Override
    public SimulationTask cloneInstance() {
        return new SimulationTask(this.strategy, this.controller);
    }
    @Override
    protected Void doInBackground() throws Exception {
            /* + concurrency in java:
                    https://docs.oracle.com/javase/tutorial/uiswing/concurrency/cancel.html
                    https://stackoverflow.com/questions/3028842/how-can-swing-dialogs-even-work
                    https://stackoverflow.com/questions/7217013/java-event-dispatching-thread-explanation
               + example code:
                    https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/concurrency/FlipperProject/src/concurrency/Flipper.java
            */
        System.out.println("    *** S I M U L A T I O N   S T A R T E D ***");
        
        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = createSimulationTask();
        ccsSim.simulate();
        return null;
    }

    private CarComponentsStorage createSimulationTask() {
        CarComponentsStorage ccsSim = new CarComponentsStorage(1000, this.strategy, this);

        SimCommand chartUpdating = new SimCommand(SimCommand.SimCommandType.AFTER_EXP) {
            @Override
            public void invoke() {
//                if (ccsSim.getCurrentReplication() % 20 == 0)
                    controller.addValueToSimDataset(ccsSim.getCurrentReplication(), ccsSim.getResult());
            }
        };
        ccsSim.storeCommand(chartUpdating);

        SimCommand consoleLogging = new SimCommand(SimCommand.SimCommandType.BEFORE_SIM) {
            @Override
            public void invoke() {
                ccsSim.setConsoleLogs(true);
            }
        };
        ccsSim.storeCommand(consoleLogging);

        SimCommand endingNotification = new SimCommand(SimCommand.SimCommandType.AFTER_SIM) {
            @Override
            public void invoke() {
                controller.onSimulationEnd();
            }
        };
        ccsSim.storeCommand(endingNotification);

        IMcExpMetaResultsCollector dailyCostsCollector = new IMcExpMetaResultsCollector() {
            @Override
            public void collectResult(double x, double y) {
                if (ccsSim.getCurrentReplication() == 1)
                    controller.addValueTo1RepDataset(x, y);
            }
        };
        ccsSim.addExperimentDataCollecting(dailyCostsCollector);

        return ccsSim;
    }

    /**
     * Crate for replication results from simulation.
     */
    public static class ReplicationResults {
        private final long replication;
        private final double costs;
        public ReplicationResults(long replication, double costs) {
            this.replication = replication;
            this.costs = costs;
        }
    }

}