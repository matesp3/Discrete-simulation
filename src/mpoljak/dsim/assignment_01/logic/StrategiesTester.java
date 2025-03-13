package mpoljak.dsim.assignment_01.logic;

import mpoljak.dsim.assignment_01.logic.experiments.*;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.simulations.CarComponentsStorage;
import mpoljak.dsim.utils.SeedGen;

public class StrategiesTester {
    private static final int DEFAULT_ORDER_A = 100; // absorbers
    private static final int DEFAULT_ORDER_B = 200; // break pads
    private static final int DEFAULT_ORDER_H = 150; // headlights

    public static void main(String[] args) {
        SeedGen seedGen = SeedGen.getInstance();
        // supplier 1
        ContinuosUniformRnd rndConfSupplier1A = new ContinuosUniformRnd(10, 70); // first 10 weeks only
        ContinuosUniformRnd rndConfSupplier1B = new ContinuosUniformRnd(30, 95); // from week 11
        Supplier supplier1 = new Supplier(11, rndConfSupplier1A, rndConfSupplier1B);
        // supplier 2
        ContinuosEmpiricalRnd rndConfSupplier2A = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80}, new double[] {10, 50, 70, 80, 95}, new double[]{0.4, 0.3, 0.2, 0.06, 0.04}
        );
        ContinuosEmpiricalRnd rndConfSupplier2B = new ContinuosEmpiricalRnd(
                new double[] {5, 10, 50, 70, 80}, new double[] {10, 50, 70, 80, 95}, new double[] {0.2, 0.4, 0.3, 0.06, 0.04}
        );
        Supplier supplier2 = new Supplier(16, rndConfSupplier2A, rndConfSupplier2B);
//       -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
        SupplyStrategy strategyA = new SingleSupply(supplier1, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
//        SupplyStrategy strategyB = new SingleSupply(supplier2, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyC = new AlternatingSupply(supplier1, supplier2, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyD = new AlternatingSupply(supplier2, supplier1, DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG1 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{1,1,1},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG2 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{1,1,2},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG3 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{1,2,1},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG4 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{1,2,2},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG5 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{2,1,1},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG6 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{2,2,1},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG7 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{2,1,2},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);
        SupplyStrategy strategyG8 = new GreaterSeasonProbSupply(supplier1, supplier2, new int[]{2,2,2},DEFAULT_ORDER_A, DEFAULT_ORDER_B, DEFAULT_ORDER_H);


        // tests execution
//        int replications = 100_000;
//        testSimulation("strategy A", strategyA, replications);
//        testSimulation("strategy B", strategyB, replications);
//        testSimulation("strategy C", strategyC, replications);
//        testSimulation("strategy D", strategyD, replications);
//        testSimulation("strategy G1", strategyD, replications);
//        testSimulation("strategy G2", strategyD, replications);
//        testSimulation("strategy G3", strategyD, replications);
//        testSimulation("strategy G4", strategyD, replications);
//        testSimulation("strategy G5", strategyD, replications);
//        testSimulation("strategy G6", strategyD, replications);
//        testSimulation("strategy G7", strategyD, replications);
//        testSimulation("strategy G8", strategyD, replications);


        int replications = 100_000;
        int i = 1; // 11*11*11 options = 1331
        for (int a = 100; a <= 200; a+=25) {
            for (int b = 100; b <= 200; b += 25) {
                for (int c = 100; c <= 200 ; c += 25) {
                    SupplyStrategy strategyB = new SingleSupply(supplier2, a, b, c);
                    CarComponentsStorage ccsSim = new CarComponentsStorage(replications, strategyB, null);
                    ccsSim.setConsoleLogs(false);
                    ccsSim.simulate();
                    System.out.printf("\n%.2f;[%d %d %d]", ccsSim.getResult(), a, b, c);
                }
            }
        }

    }

    private static void testSimulation(String strategyName, SupplyStrategy strategy, int replications) {
        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = new CarComponentsStorage(replications, strategy, null);
        ccsSim.setConsoleLogs(false);
        ccsSim.simulate();
        System.out.println(" * AVG costs ("+strategyName+"): "+Math.ceil(ccsSim.getResult())+" [€]");
    }

}
