package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

public abstract class StatResult {
    private String description;
    private String unit;

    public StatResult(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public static class Simple extends StatResult {
        private double value;

        public Simple(String description, double value, String unit) {
            super(description, unit);
            this.value = value;
        }

        public double getValue() {
            return this.value;
        }
    }

    public static class ConfInterval extends StatResult {
        private double mean;
        private double halfWidth;

        public ConfInterval(String description, double mean, double halfWidth, String unit) {
            super(description, unit);
            this.mean = mean;
            this.halfWidth = halfWidth;
        }

        public double getMean() {
            return this.mean;
        }

        public double getHalfWidth() {
            return this.halfWidth;
        }
    }
}
