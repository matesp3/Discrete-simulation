package mpoljak.dsim.assignment_01.logic.experiments;

public class HalfSeasonSupply extends SupplyStrategy {
    private final Supplier firstSupplier;
    private final Supplier secondSupplier;
    private final int amountAbsorbers;
    private final int amountBrakePads;
    private final int amountHeadlights;

    public HalfSeasonSupply(Supplier firstSupplier, Supplier secondSupplier, int amountA, int amountB, int amountH) {
        if (firstSupplier == null || secondSupplier == null)
            throw new IllegalArgumentException("Supplier's instance not provided");
        this.firstSupplier = firstSupplier;
        this.secondSupplier = secondSupplier;
        this.amountAbsorbers = amountA;
        this.amountBrakePads = amountB;
        this.amountHeadlights = amountH;
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        Supplier supplier = week < 16 ? firstSupplier : secondSupplier;
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
