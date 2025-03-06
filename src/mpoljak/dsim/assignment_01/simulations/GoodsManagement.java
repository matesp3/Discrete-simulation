package mpoljak.dsim.assignment_01.simulations;

import mpoljak.dsim.assignment_01.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.assignment_01.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.generators.DiscreteEmpiricalRnd;
import mpoljak.dsim.assignment_01.generators.DiscreteUniformRnd;
import mpoljak.dsim.common.MCSimCore;
import mpoljak.dsim.utils.DoubleComp;

import java.util.Random;

public class GoodsManagement extends MCSimCore {
    private final static double STORAGE_COST_ABSORBERS = 0.2; // € per DAY per product
    private final static double STORAGE_COST_BRAKE_PADS = 0.3; // € per DAY per product
    private final static double STORAGE_COST_HEADLIGHTS = 0.25; // € per DAY per product
    private final static double PENALTY = 0.3;  // € per product of whatever type
    private final static int ORDER_AMOUNT_ABSORBERS = 100;
    private final static int ORDER_AMOUNT_BRAKE_PADS = 200;
    private final static int ORDER_AMOUNT_HEADLIGHTS = 150;
    private final static int OBSERVED_WEEKS = 6;

    private final DiscreteUniformRnd rndAbsorbers;
    private final DiscreteUniformRnd rndBrakePads;
    private final DiscreteEmpiricalRnd rndHeadlights;

    private final ContinuosUniformRnd rndConfSupplier1A;
    private final ContinuosUniformRnd rndConfSupplier1B;
    private final ContinuosEmpiricalRnd rndConfSupplier2A;
    private final ContinuosEmpiricalRnd rndConfSupplier2B;

    private final ContinuosUniformRnd rndDeliverySuccessA;
    private final ContinuosUniformRnd rndDeliverySuccessB;

    private int storedAbsorbers;
    private int storedBrakePads;
    private int storedHeadlights;

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
        this.storedAbsorbers = 0;
        this.storedBrakePads = 0;
        this.storedHeadlights = 0;
    }

    @Override
    protected void experiment() {
        System.out.println(" E X P E R I M E N T ["+this.getCurrentReplication()+"]");
        int amountAbsorbers, amountBrakePads, amountHeadlights;
        double confidentiality, deliveryDecision;

        for (int i = 0; i < OBSERVED_WEEKS; i++) { // week algorithm
            System.out.println("  +WEEK - "+(i+1));
            // generated PERCENT PROBABILITIES of delivery success
            if (i+1 < 3) {  // season: A. 3 is the first week of B confidentiality
                confidentiality =  this.rndConfSupplier1A.sample();
                deliveryDecision = this.rndDeliverySuccessA.sample();
//                if ( < ) // '<' bcs first value is in 0,so we have to exclude last one
            }
            else {          // season: B
                confidentiality =  this.rndConfSupplier1B.sample();  // todo this could be wrapped into SupplierClass, so that we can implement STRATEGY design pattern
                deliveryDecision = this.rndDeliverySuccessB.sample();
            }
            if (DoubleComp.compare(deliveryDecision, confidentiality) == -1) { // order is supplied
                this.storedAbsorbers += ORDER_AMOUNT_ABSORBERS;
                this.storedBrakePads += ORDER_AMOUNT_BRAKE_PADS;
                this.storedHeadlights += ORDER_AMOUNT_HEADLIGHTS;
            }   // else -> ROLLBACK operation of week order, if it's not delivered on Monday -> stored amounts not changed

            System.out.printf("      |_> (deliveryCond=%.02f < conf=%.02f%%) => supplied=%B  => NEW STORED AMOUNTS: [A=%d  B=%d  H=%d]\n",
                    deliveryDecision, confidentiality, (DoubleComp.compare(deliveryDecision, confidentiality) == -1), this.storedAbsorbers, this.storedBrakePads, this.storedHeadlights);
            // generated WANTED AMOUNTS of products by CUSTOMER on week basis
            amountAbsorbers = (int)this.rndAbsorbers.sample();
            amountBrakePads = (int)this.rndBrakePads.sample();
            amountHeadlights = (int)this.rndHeadlights.sample();
//            this.storedAbsorbers -= amountAbsorbers;
//            this.storedBrakePads -= amountBrakePads;
//            this.storedHeadlights -= amountHeadlights;
        }
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
        GoodsManagement gm = new GoodsManagement(new Random(), 1);
        gm.simulate();
    }
}
