package mpoljak.dsim.assignment_01.logic.experiments;

/**
 * Supplying car components by SupplierA every odd week and supplying by SupplierB every even week.
 */
public class AlternatingSupply extends SupplyStrategy {
    private final Supplier supplierOdd;
    private final Supplier supplierEven;
    private final int amountAbsorbers;
    private final int amountBrakePads;
    private final int amountHeadlights;
    /**
     * @param supplierOdd this supplier supplies car components every odd week
     * @param supplierEven this supplier supplies car components every even week
     * @param amountA amount of supplied absorbers within one delivery
     * @param amountB amount of supplied break pads within one delivery
     * @param amountH amount of supplied headlights within one delivery
     */
    public AlternatingSupply(Supplier supplierOdd, Supplier supplierEven, int amountA, int amountB, int amountH) {
        if (supplierOdd == null || supplierEven == null)
            throw new IllegalArgumentException("Supplier's instance not provided");
        this.supplierOdd = supplierOdd;
        this.supplierEven = supplierEven;
        this.amountAbsorbers = amountA;
        this.amountBrakePads = amountB;
        this.amountHeadlights = amountH;
    }

    @Override
    public SupplierResult supply(int week, boolean printDecisionDetails) {
        Supplier chosenSupplier = (week%2 == 1) ? supplierOdd : supplierEven;
        if (chosenSupplier.orderDelivered(week, printDecisionDetails)) {
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
