package mpoljak.dsim.assignment_01.logic.tasks;

import mpoljak.dsim.assignment_01.controllers.SimController;
import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.simulations.CarComponentsStorage;
import mpoljak.dsim.common.ICloneable;
import mpoljak.dsim.common.SimCommand;
import mpoljak.dsim.utils.SeedGen;

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

    @Override
    protected Void doInBackground() throws Exception {
            /* TODO: do konstruktora simulacie si skusit poslat odkaz na tento task a v metode simulate() kontrolovat
                    stav vlakna pomocou isCancelled(). V metode afterReplication() pri kazdej 1000-tej replikacii
                    zavolat metodu publish().
                    concurrency in java: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/cancel.html
                                         https://stackoverflow.com/questions/3028842/how-can-swing-dialogs-even-work
                                         https://stackoverflow.com/questions/7217013/java-event-dispatching-thread-explanation
                    Vzorovy kod: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/concurrency/FlipperProject/src/concurrency/Flipper.java
            */
        System.out.println("    *** S I M U L A T I O N   S T A R T E D ***");

        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = new CarComponentsStorage(1000, this.strategy, this);
        SimCommand chartUpdating = new SimCommand(SimCommand.SimCommandType.AFTER_EXP) {
            @Override
            public void invoke() {
                if (ccsSim.getCurrentReplication() % 20 == 0)
                    controller.addValueToSimDataset(ccsSim.getCurrentReplication(), ccsSim.getResult());
            }
        };
        ccsSim.registerCommand(chartUpdating);
        ccsSim.simulate();
        return null;
    }

    @Override
    public SimulationTask cloneInstance() {
        return new SimulationTask(this.strategy, this.controller);
    }

    public void setStrategy(SupplyStrategy strategy) {
        if (strategy == null)
            return;
        this.strategy = strategy;
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