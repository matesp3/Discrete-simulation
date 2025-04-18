package mpoljak.dsim.assignment_01.logic.experiments;

/**
 * Supplying car components by only one supplier every week.
 */
public class SingleSupply extends SupplyStrategy {
    private final Supplier supplier;
    private final int amountAbsorbers;
    private final int amountBrakePads;
    private final int amountHeadlights;

    /**
     *
     * @param supplier instance of Supplier supplying car components every week
     * @param amountA amount of supplied absorbers within one delivery
     * @param amountB amount of supplied break pads within one delivery
     * @param amountH amount of supplied headlights within one delivery
     */
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
        if (this.supplier.orderDelivered(week, printDecisionDetails)) {
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
