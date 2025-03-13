package mpoljak.dsim.assignment_01.logic.experiments;

import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.SeedGen;

import java.util.Random;

public class GreaterSeasonProbSupply extends SupplyStrategy {
    private final Supplier supplier1;
    private final Supplier supplier2;
    private final int amountAbsorbers;
    private final int amountBrakePads;
    private final int amountHeadlights;
    private final int[] withGreaterProb;
    private final Random rand;

    public GreaterSeasonProbSupply(Supplier supplier1, Supplier supplier2, int[] withGreaterProb, int amountA, int amountB, int amountH) {
        if (supplier1 == null || supplier2 == null)
            throw new IllegalArgumentException("Supplier's instance not provided");
        this.supplier1 = supplier1;
        this.supplier2 = supplier2;
        this.withGreaterProb = withGreaterProb;
        this.amountAbsorbers = amountA;
        this.amountBrakePads = amountB;
        this.amountHeadlights = amountH;
        this.rand = new Random(SeedGen.getInstance().nextSeed());
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        int season = week < 11 ? 0 : (week < 21 ? 1 : 2);
        double decision = this.rand.nextDouble();
        Supplier supplier;
        if (DoubleComp.compare(decision, 3.0/4.0) == -1) { // (decision < 2.0/3.0)
            supplier = this.withGreaterProb[season] == 1 ? this.supplier1 : this.supplier2;
        }
        else
            supplier = this.withGreaterProb[season] == 1 ? this.supplier2 : this.supplier1;

        if (supplier.orderDelivered(week, printDecisionDetails)) {
            this.results.setDeliveredAmounts(this.amountAbsorbers, this.amountBrakePads, this.amountHeadlights);
            if (printDecisionDetails)
                System.out.printf(" SUPPLIED: [A=%dx B=%dx H=%dx]", this.amountAbsorbers, this.amountBrakePads,
                        this.amountHeadlights);
        }
        else
            this.results.resetResults();
        return this.results;
    }
}
