package mpoljak.dsim.assignment_01.logic.simulations;

import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.generators.DiscreteEmpiricalRnd;
import mpoljak.dsim.assignment_01.logic.generators.DiscreteUniformRnd;
import mpoljak.dsim.common.MCSimCore;

import java.time.DayOfWeek;
import java.util.Random;

public class CarComponentsStorage extends MCSimCore {
    private final static double STORAGE_COST_ABSORBERS = 0.2; // € per DAY per product
    private final static double STORAGE_COST_BRAKE_PADS = 0.3; // € per DAY per product
    private final static double STORAGE_COST_HEADLIGHTS = 0.25; // € per DAY per product
    private final static double PENALTY = 0.3;  // € per product of whatever type
    private final static int OBSERVED_WEEKS = 30;

    private final DiscreteUniformRnd rndAbsorbers;
    private final DiscreteUniformRnd rndBrakePads;
    private final DiscreteEmpiricalRnd rndHeadlights;

    private SupplyStrategy supplyStrategy;

    public CarComponentsStorage(long repCount, SupplyStrategy supplyStrategy) {
        super(repCount);

        if (supplyStrategy == null)
            throw new IllegalArgumentException("Supply strategy not provided");

        this.supplyStrategy = supplyStrategy;
        this.rndAbsorbers = new DiscreteUniformRnd(50, 100);
        this.rndBrakePads = new DiscreteUniformRnd(60, 250);
        this.rndHeadlights = new DiscreteEmpiricalRnd(
                new double[]{30, 60, 100, 140},
                new double[]{60, 100, 140, 160},
                new double[]{0.2, 0.4, 0.3, 0.1}
        );
        // todo: here will be some method to load configuration
    }

    @Override
    protected void experiment() {
        boolean log = false;
        if (log) System.out.println(" E X P E R I M E N T ["+this.getCurrentReplication()+"]");
        int orderAbsorbers, orderBrakePads, orderHeadlights;
        double confidentiality, deliveryDecision;
        // costs can only increase, not decrease
        double costs = 0; // costs for storing (after morning) of products & for not providing desired amount of products on friday
        // todo mozem v semestralke spomenut, ze najlepsie by bolo nevyrabat nic, ale potrebujeme uspokojit zakaznika
        int storedAbsorbers = 0;
        int storedBrakePads = 0;
        int storedHeadlights = 0;

        for (int w = 1; w < OBSERVED_WEEKS+1; w++) { // week algorithm
            if (log) System.out.println("  + WEEK-"+(w)+":");
            for (int day = 1; day < 8; day++) {
                if (log) this.printDayOfWeek(day);
                // mondayStrategy - contains strategy for choosing supplier & amount of ordered amounts
                if (day == DayOfWeek.MONDAY.getValue()) {
//                    // DELIVERY OF PRODUCTS [generated PERCENT PROBABILITIES of delivery success
                    if (log) System.out.print("\n         ^-- INTRA-DAY:");

                    SupplyStrategy.SupplierResult res = this.supplyStrategy.supply(w, log);
                    if (res.areProductsDelivered()) { // order is supplied
                        storedAbsorbers += res.getSuppliedAbsorbers();
                        storedBrakePads += res.getSuppliedBrakePads();
                        storedHeadlights += res.getSuppliedHeadlights();
                    }   // else -> ROLLBACK operation of week order, if it's not delivered on Monday -> stored amounts not changed
                }
                else if (day == DayOfWeek.FRIDAY.getValue()) {
//                     SELLING PRODUCTS [generated AMOUNTS of products ORDERED by CUSTOMER]
                    orderAbsorbers = (int)this.rndAbsorbers.sample();
                    orderBrakePads = (int)this.rndBrakePads.sample();
                    orderHeadlights = (int)this.rndHeadlights.sample();
                    storedAbsorbers -= orderAbsorbers;
                    storedBrakePads -= orderBrakePads;
                    storedHeadlights -= orderHeadlights;
//                    paying penalties for not providing ordered product
                    if (Double.compare(storedAbsorbers, 0) == -1) { // (storedAbsorbers < 0)
                        costs += (-1)*(storedAbsorbers)*PENALTY;
                        storedAbsorbers = 0;
                    }
                    if (Double.compare(storedBrakePads, 0) == -1) { // (storedBrakePads < 0)
                        costs += (-1)*(storedBrakePads)*PENALTY;
                        storedBrakePads = 0;
                    }
                    if (Double.compare(storedHeadlights, 0) == -1) { // (storedHeadlights < 0)
                        costs += (-1)*(storedHeadlights)*PENALTY;
                        storedHeadlights = 0;
                    }
                    if (log) System.out.printf("\n         ^-- INTRA-DAY: [required amounts: [A=%dx B=%dx H=%d]]",
                            orderAbsorbers, orderBrakePads, orderHeadlights);
                }
//                at the end of every day, we have to pay storing costs
                costs += storedAbsorbers * STORAGE_COST_ABSORBERS + storedBrakePads * STORAGE_COST_BRAKE_PADS +
                            storedHeadlights * STORAGE_COST_HEADLIGHTS;
                if (log) { System.out.printf("\n         ^-- ");
                            this.printEndOfDayState(storedAbsorbers, storedBrakePads, storedHeadlights, costs);
                }
            }
        }
        this.cumulate(costs);
    }

    private void printEndOfDayState(int amountA, int amountB, int amountH, double costs) {
        System.out.printf("EVENING: [costs=%.2f  STORED: [A=%dx B=%dx H=%dx]]\n"
                , costs, amountA, amountB, amountH);
    }
    private void printDayOfWeek(int day) {
        System.out.printf("       %s", DayOfWeek.of(day).name().substring(0, 3));
    }

    @Override
    protected void afterSimulation() {

    }

    @Override
    protected void beforeExperiment() {

    }

    @Override
    protected void afterExperiment() {

    }

    public static void main(String[] args) {
        final int defaultOrderA = 100; // absorbers
        final int defaultOrderB = 200; // break pads
        final int defaultOrderH = 150; // headlights
        Random seedGen = new Random();
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
        SupplyStrategy strategyA = new SingleSupply(supplier1, defaultOrderA, defaultOrderB, defaultOrderH);
        // strategy B
        SupplyStrategy strategyB = new SingleSupply(supplier2, defaultOrderA, defaultOrderB, defaultOrderH);
        // simulation Monte Carlo for John trading car components
        CarComponentsStorage ccsSim = new CarComponentsStorage(10_000_000, strategyA);
//        GoodsManagement gm = new GoodsManagement(new Random(), 1_000_000);
        ccsSim.simulate();
        System.out.println("AVG costs [strategy A]: "+Math.ceil(ccsSim.getResult())+" [€]");
    }
}
