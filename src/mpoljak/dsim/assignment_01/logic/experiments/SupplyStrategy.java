package mpoljak.dsim.assignment_01.logic.experiments;

/**
 * Strategy of supply based on week number and probability of potential success in delivering.
 */
public abstract class SupplyStrategy {
    protected final SupplierResult results; // recycled result's instance for every supply() call

    public SupplyStrategy() {
        this.results = new SupplierResult(); // implicit initialization
    }

    /**
     * This is an algorithm of decision whether to supply in specified <code>week</code> based on internal probability
     * distribution of chosen week's supplier.
     * @param week number of observed week
     * @param printDecisionDetails gives details through console about generated probability of delivery success in
     *                             percents and about reality, if delivery was successful.
     * @return results of delivery's reality
     */
    public abstract SupplierResult supply(int week, boolean printDecisionDetails);

//    - -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    /**
     * Implemented as Crate (design pattern) for retrieving results of <code>SupplierStrategy.supply()</code> method.
     */
    public static class SupplierResult {
        private int suppliedAbsorbers;
        private int suppliedBrakePads;
        private int suppliedHeadlights;
        private boolean productsDelivered;

        public SupplierResult() {
            this.resetResults();
        }

        /**
         * @return amount of delivered absorbers
         */
        public int getSuppliedAbsorbers() {
            return this.suppliedAbsorbers;
        }

        /**
         * @return amount of delivered brake pads.
         */
        public int getSuppliedBrakePads() {
            return this.suppliedBrakePads;
        }

        /**
         * @return amount of delivered headlights.
         */
        public int getSuppliedHeadlights() {
            return this.suppliedHeadlights;
        }

        /**
         * @return reality of success of delivering products
         */
        public boolean areProductsDelivered() {
            return this.productsDelivered;
        }

        protected void resetResults() {
            this.suppliedAbsorbers = 0;
            this.suppliedBrakePads = 0;
            this.suppliedHeadlights = 0;
            this.productsDelivered = false;
        }

        /**
         * Sets delivered amounts to specified numbers and sets delivery as successful.
         * @param amountA absorbers
         * @param amountB brake pads
         * @param amountH headlights
         */
        protected void setDeliveredAmounts(int amountA, int amountB, int amountH) {
            this.suppliedAbsorbers = amountA;
            this.suppliedBrakePads = amountB;
            this.suppliedHeadlights = amountH;
            this.productsDelivered = true;
        }
    }
}
