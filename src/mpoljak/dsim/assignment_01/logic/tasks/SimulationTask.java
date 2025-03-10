package mpoljak.dsim.assignment_01.logic.tasks;

import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.simulations.CarComponentsStorage;
import mpoljak.dsim.utils.SeedGen;

import javax.swing.*;

public class SimulationTask extends SwingWorker<Void, SimulationTask.ReplicationResults> {
    @Override
    protected Void doInBackground() throws Exception {
            /* TODO: do konstruktora simulacie si skusit poslat odkaz na tento task a v metode simulate() kontrolovat
                    stav vlakna pomocou isCancelled(). V metode afterReplication() pri kazdej 1000-tej replikacii
                    zavolat metodu publish().
                    Vzorovy kod: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/concurrency/FlipperProject/src/concurrency/Flipper.java
            */
        System.out.println("    *** S I M U L A T I O N   S T A R T E D ***");
        SeedGen seedGen = SeedGen.getInstance();
        // supplier 1
        ContinuosUniformRnd rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        ContinuosUniformRnd rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        Supplier supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
//       -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
        SupplyStrategy strategyA = new SingleSupply(supplier1, 100, 200, 150);
        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = new CarComponentsStorage(1000, strategyA, this);
        ccsSim.simulate();
        return null;
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