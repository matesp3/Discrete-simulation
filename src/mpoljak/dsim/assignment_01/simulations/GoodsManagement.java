package mpoljak.dsim.assignment_01.simulations;

import mpoljak.dsim.assignment_01.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.generators.DiscreteEmpiricalRnd;
import mpoljak.dsim.assignment_01.generators.DiscreteUniformRnd;
import mpoljak.dsim.common.MCSimCore;
import mpoljak.dsim.utils.DoubleComp;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Random;

public class GoodsManagement extends MCSimCore {
    private final static double STORAGE_COST_ABSORBERS = 0.2; // € per DAY per product
    private final static double STORAGE_COST_BRAKE_PADS = 0.3; // € per DAY per product
    private final static double STORAGE_COST_HEADLIGHTS = 0.25; // € per DAY per product
    private final static double PENALTY = 0.3;  // € per product of whatever type
    private final static int ORDER_AMOUNT_ABSORBERS = 100;
    private final static int ORDER_AMOUNT_BRAKE_PADS = 200;
    private final static int ORDER_AMOUNT_HEADLIGHTS = 150;
    private final static int OBSERVED_WEEKS = 30;

    private final DiscreteUniformRnd rndAbsorbers;
    private final DiscreteUniformRnd rndBrakePads;
    private final DiscreteEmpiricalRnd rndHeadlights;

    private final ContinuosUniformRnd rndConfSupplier1A;
    private final ContinuosUniformRnd rndConfSupplier1B;
    private final ContinuosEmpiricalRnd rndConfSupplier2A;
    private final ContinuosEmpiricalRnd rndConfSupplier2B;

    private final ContinuosUniformRnd rndDeliverySuccessA;
    private final ContinuosUniformRnd rndDeliverySuccessB;

    public GoodsManagement(Random seedGen, long repCount) {
        super(repCount);

        this.rndAbsorbers = new DiscreteUniformRnd(seedGen, 50, 100);
        this.rndBrakePads = new DiscreteUniformRnd(seedGen, 60, 250);
        this.rndHeadlights = new DiscreteEmpiricalRnd(seedGen,
                new double[]{30, 60, 100, 140},
                new double[]{60, 100, 140, 160},
                new double[]{0.2, 0.4, 0.3, 0.1}
        );
        this.rndConfSupplier1A = new ContinuosUniformRnd(seedGen, 10, 70); // first 10 weeks only
        this.rndConfSupplier1B = new ContinuosUniformRnd(seedGen, 30, 95); // from week 11
        this.rndConfSupplier2A = new ContinuosEmpiricalRnd(seedGen,
                new double[] {5, 10, 50, 70, 80},
                new double[] {10, 50, 70, 80, 95},
                new double[]{0.4, 0.3, 0.2, 0.06, 0.04}
        );
        this.rndConfSupplier2B = new ContinuosEmpiricalRnd(seedGen,
                new double[] {5, 10, 50, 70, 80},
                new double[] {10, 50, 70, 80, 95},
                new double[] {0.2, 0.4, 0.3, 0.06, 0.04}
        );
        this.rndDeliverySuccessA = new ContinuosUniformRnd(seedGen, 0, 100);
        this.rndDeliverySuccessB = new ContinuosUniformRnd(seedGen, 0, 100);

        // todo: here will be some method to load configuration
    }

    @Override
    protected void experiment() {
        boolean log = false;
        if (log) System.out.println(" E X P E R I M E N T ["+this.getCurrentReplication()+"]");
        int orderAbsorbers, orderBrakePads, orderHeadlights;
        double confidentiality, deliveryDecision;
        // costs can only increase, not decrease
        double costs = 0; // costs for storing (after morning) products & for not providing wanted amount of products on friday
        // todo mozem v semestralke spomenut, ze najlepsie by bolo nevyrabat nic, ale potrebujeme uspokojit zakaznika
        int storedAbsorbers = 0;
        int storedBrakePads = 0;
        int storedHeadlights = 0;

        for (int w = 0; w < OBSERVED_WEEKS; w++) { // week algorithm
            if (log) System.out.println("  + WEEK-"+(w+1)+":");
            for (int day = 1; day < 8; day++) {
                if (log) this.printDayOfWeek(day);
                // mondayStrategy - contains strategy for choosing supplier & amount of ordered amounts
                if (day == DayOfWeek.MONDAY.getValue()) {
//                    // DELIVERY OF PRODUCTS [generated PERCENT PROBABILITIES of delivery success
                    if (w < 10) {  // season: A. 3 is the first week of B confidentiality
//                    if ( < ) // '<' bcs first value is in 0.0, so we have to exclude last one
                        confidentiality =  this.rndConfSupplier1A.sample();
                        deliveryDecision = this.rndDeliverySuccessA.sample();
                    }
                    else {          // season: B
                        confidentiality =  this.rndConfSupplier1B.sample();  // todo this could be wrapped into SupplierClass, so that we can implement STRATEGY design pattern
                        deliveryDecision = this.rndDeliverySuccessB.sample();
                    }

                    if (DoubleComp.compare(deliveryDecision, confidentiality) == -1) { // order is supplied
                        storedAbsorbers += ORDER_AMOUNT_ABSORBERS;
                        storedBrakePads += ORDER_AMOUNT_BRAKE_PADS;
                        storedHeadlights += ORDER_AMOUNT_HEADLIGHTS;
                    }   // else -> ROLLBACK operation of week order, if it's not delivered on Monday -> stored amounts not changed

                    if (log) System.out.printf("\n         ^-- INTRA-DAY: (reality=%.02f < confidence=%.02f%%) => supplied=%B",
                            deliveryDecision, confidentiality, (DoubleComp.compare(deliveryDecision, confidentiality) == -1));
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
                    if (log) System.out.printf("\n         ^-- INTRA-DAY: [required amounts: [A=%d  B=%d  H=%d]]",
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
        System.out.printf("EVENING: [costs=%.2f  STORED: [A=%d   B=%d    H=%d]]\n"
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
        GoodsManagement gm = new GoodsManagement(new Random(), 1_000_000);
        gm.simulate();
        System.out.println("avg costs [strategy A]: "+gm.getResult());
    }
}
