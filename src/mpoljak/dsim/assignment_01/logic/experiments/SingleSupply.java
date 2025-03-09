package mpoljak.dsim.assignment_01.logic.experiments;

public class SingleSupply extends SupplyStrategy {
    private final Supplier supplier;
    private final int amountAbsorbers;
    private final int amountBrakePads;
    private final int amountHeadlights;

    public SingleSupply(Supplier supplier, int amountA, int amountB, int amountH) {
        if (supplier == null)
            throw new NullPointerException("Supplier's instance not provided.");
        this.supplier = supplier;
        this.amountAbsorbers = amountA;
        this.amountBrakePads = amountB;
        this.amountHeadlights = amountH;
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        if (supplier.orderDelivered(week, printDecisionDetails)) {
            this.results.suppliedAbsorbers = this.amountAbsorbers;
            this.results.suppliedBrakePads = this.amountBrakePads;
            this.results.suppliedHeadlights = this.amountHeadlights;
            this.results.productsDelivered = true;
            if (printDecisionDetails)
                System.out.printf(" SUPPLIED: [A=%dx B=%dx H=%dx]", this.amountAbsorbers, this.amountBrakePads,
                    this.amountHeadlights);
        }
        else
            this.results.resetResults();
        return this.results;
    }
}
