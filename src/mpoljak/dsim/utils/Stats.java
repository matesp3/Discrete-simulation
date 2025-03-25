package mpoljak.dsim.utils;

public class Stats {
    // private static final T_ALFA = {x, y, z, first 30-values} ;// depending on current count up to first 30 values
    private double sum;
    private double sumOfSquares;
    private long count;

    public Stats() {
        this.sum = 0;
        this.sumOfSquares = 0;
        this.count = 0;
    }

    /**
     * Adds new <code>value</code> to calculated statistics.
     * @param value
     */
    public void addSample(double value) {
        this.sum += value;
        this.sumOfSquares += Math.pow(value, 2);
        this.count++;
    }

    public double getSum() {
        return this.sum;
    }

    public double getMean() {
        return this.sum / this.count;
    }

    public double getVariance() {
        return (this.sumOfSquares - Math.pow(this.sum, 2)/this.count) / (this.count - 1);
    }

    public double getStdDev() {
        return Math.pow(this.getVariance(), 0.5);
    }

    /**
     * @param confidence percents
     * @return [0] - left bound, [1] right bound
     */
    public double[] getConfidenceInterval(double confidence) {
        double left = 0;  // todo left
        double right = 0; // todo right
        return new double[]{left, right};
    }

    private double getHalfWidthCI() {
        return (this.getStdDev()*1.96)/Math.sqrt(this.count);
    }

    @Override
    public String toString() {
        return String.format("Stats summary:\n  * sum=%.3f\n  * count=%d\n  * sum^2=%.3f\n  * avg=%.3f\n  * var=%.3f" +
                        "\n  * stdev=%.3f\n  * halfWidth=%.3f",
                this.getSum(), this.count, this.sumOfSquares, this.getMean(), this.getVariance(), this.getStdDev(), this.getHalfWidthCI());
    }

    public static void main(String[] args) {
        Stats stats = new Stats();
        for (int i = 1; i <= 10; i++) {
            stats.addSample(i);
        }
        System.out.println(stats);
    }
}
