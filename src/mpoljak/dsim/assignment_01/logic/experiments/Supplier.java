package mpoljak.dsim.assignment_01.logic.experiments;

import mpoljak.dsim.generators.ContinuosUniformRnd;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;

/**
 * Purpose of <code>Supplier</code> class is to generate decisions about delivery success by using generators of
 * desirable probability distribution and number of week.
 */
public class Supplier {
    private final int thresholdWeek;

    private final Generator rndConfSupplier1A; // percent confidence of delivery by supplier before
    private final Generator rndConfSupplier1B; // percent confidence of delivery by supplier starting on threshold week

    private final ContinuosUniformRnd rndDeliverySuccessA; // generator of reality before threshold week
    private final ContinuosUniformRnd rndDeliverySuccessB; // generator of reality starting on threshold week

    /**
     * @param thresholdWeek first week, when will be used probability distribution specified by
     *                      <code>rndConfDelivery2</code>.
     * @param rndConfDelivery1 probability distribution of percent confidence for generating delivery decisions until
     *                        week specified by param <code>thresholdWeek</code>.
     * @param rndConfDelivery2 probability distribution of percent confidence for generating delivery decisions  from
     *                         week <code>thresholdWeek</code>
     */
    public Supplier(int thresholdWeek, Generator rndConfDelivery1, Generator rndConfDelivery2) {
        this.thresholdWeek = thresholdWeek;
        this.rndConfSupplier1A = rndConfDelivery1;
        this.rndConfSupplier1B = rndConfDelivery2;
        this.rndDeliverySuccessA = new ContinuosUniformRnd(0, 100);
        this.rndDeliverySuccessB = new ContinuosUniformRnd(0, 100);
    }

    /**
     * Generates confidence of delivery in percents determined by <code>week</code> and probability distribution.
     * Then generates decision and determines, what is reality: if order is going to be delivered or not.
     * @param week number of week starting with value <code>1</code>
     * @param printDecision if it's required to print decisions made on supply process on console output
     * @return <code>true</code> if order is going to be delivered.
     */
    public boolean orderDelivered(int week, boolean printDecision) {
        double confidentiality, deliveryDecision;
        if (week < thresholdWeek) {  // season: A
            confidentiality =  this.rndConfSupplier1A.sample();
            deliveryDecision = this.rndDeliverySuccessA.sample();
        }
        else {                      // season: B
            confidentiality =  this.rndConfSupplier1B.sample();
            deliveryDecision = this.rndDeliverySuccessB.sample();
        }
        if (printDecision) System.out.printf("(reality=%.02f < confidence=%.02f%%) => supplied=%B",
                deliveryDecision, confidentiality, (DoubleComp.compare(deliveryDecision, confidentiality) == -1));
        return (DoubleComp.compare(deliveryDecision, confidentiality) == -1);
    }
}
