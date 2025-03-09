package mpoljak.dsim.assignment_01.logic;

import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.simulations.CarComponentsStorage;
import mpoljak.dsim.utils.SeedGen;

import java.util.Random;

public class StrategiesTester {
    private static final int DEFAULT_ORDER_A = 100; // absorbers
    private static final int DEFAULT_ORDER_B = 200; // break pads
    private static final int DEFAULT_ORDER_H = 150; // headlights

    public static void main(String[] args) {

    }

    private static void strategyA(Supplier singleSupplier) {

    }

    private static void strategyB(Supplier singleSupplier) {

        SeedGen seedGen = SeedGen.getInstance();
        // supplier 1
        ContinuosUniformRnd rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        ContinuosUniformRnd rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        Supplier supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
        // supplier 2
        ContinuosEmpiricalRnd rndConfSupplier2A = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80},
                new double[] {10, 50, 70, 80, 95},
                new double[]{0.4, 0.3, 0.2, 0.06, 0.04}
        );
        ContinuosEmpiricalRnd rndConfSupplier2B = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80},
                new double[] {10, 50, 70, 80, 95},
                new double[] {0.2, 0.4, 0.3, 0.06, 0.04}
        );
        Supplier supplier2 = new Supplier(16, rndConfSupplier2A, rndConfSupplier2B);
        // strategy A
        SupplyStrategy strategyA = new SingleSupply(supplier1, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        // strategy B
        SupplyStrategy strategyB = new SingleSupply(supplier2, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = new CarComponentsStorage(100_000, strategyA);
//        GoodsManagement gm = new GoodsManagement(new Random(), 1_000_000);
        ccsSim.simulate();
        System.out.println("avg costs [strategy A]: "+ccsSim.getResult());
    }
}
