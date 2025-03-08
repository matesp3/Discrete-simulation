package mpoljak.dsim.assignment_01.experiments;

public abstract class SupplierStrategy {
    protected final SupplierResult results; // recycled result's instance for every supply() call

    public SupplierStrategy() {
        this.results = new SupplierResult(); // implicit initialization
    }

    public abstract SupplierResult supply();

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

        public int getSuppliedAbsorbers() {
            return this.suppliedAbsorbers;
        }

        public int getSuppliedBrakePads() {
            return this.suppliedBrakePads;
        }

        public int getSuppliedHeadlights() {
            return this.suppliedHeadlights;
        }

        public boolean areProductsDelivered() {
            return this.productsDelivered;
        }

        protected void resetResults() {
            this.suppliedAbsorbers = 0;
            this.suppliedBrakePads = 0;
            this.suppliedHeadlights = 0;
            this.productsDelivered = false;
        }
    }
    public static class StratA extends SupplierStrategy {

        public StratA(int number) {
            System.out.println("Tu1");
        }

        @Override
        public SupplierResult supply() {
            this.results.suppliedHeadlights = 5;
            return this.results;
        }
    }

    public static void main(String[] args) {
        StratA strategy = new StratA(1);
        SupplierResult res = strategy.supply();
    }
}
