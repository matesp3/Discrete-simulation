package mpoljak.dsim.utils;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.DiscreteUniformRnd;

public abstract class Stats {
    protected double sum;
    protected double count;
    protected long samplesCount;

    public Stats() {
        this.sum = 0;
        this.count = 0;
        this.samplesCount = 0;
    }

    public double getMean() {
        return this.sum / this.count;
    }

    public double getSum() {
        return this.sum;
    }

    public double getCount() {
        return this.count;
    }

    public long getSamplesCount() {
        return this.samplesCount;
    }

    @Override
    public String toString() {
        return String.format("Stats:\n  * sum=%.03f\n  * count=%.03f\n  * mean=%.03f",
                this.getSum(), this.count, this.getMean());
    }
//  -   -   -   -   -   -   -   ARI AVG -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static class ArithmeticAvg extends Stats {
        /**
         * Adds new sample to this statistics.
         * @param value
         */
        public void addSample(double value) {
            this.sum += value;
            this.count++;
            this.samplesCount++;
        }

    @Override
    public String toString() {
        return "ArithmeticAvg "+ super.toString();
    }
}
//  -   -   -   -   -   -   -   ARI AVG -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static class WeightedAvg extends Stats {
        /**
         * Adds new sample with its corresponding weight to this statistics.
         * @param value
         * @param weight
         */
        public void addSample(double value, double weight) {
            this.sum += value * weight;
            this.count += weight;
            this.samplesCount++;
        }

    @Override
    public String toString() {
        return "WeightedAvg "+ super.toString();
    }
    }
//  -   -   -   -   -   -   -   C I -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public abstract static class ConfidenceInterval {
        protected static final double T_ALFA = 1.96 ; // from 30 samples
        protected double sumOfSquares;

        public ConfidenceInterval() {
            super();
            this.sumOfSquares = 0;
        }

        /**
         * @return standard deviation
         */
        public double getStdDev() {
                return Math.pow(this.getVariance(), 0.5);
        }

        public abstract double getVariance();

        /**
         * @return half-width of 95% confidence interval.
         */
        public abstract double getHalfWidthCI();
        /**
         * Computes 95% confidence interval from current state of this instance.
         * @return [0] - lower bound, [1] upper bound of 95% confidence interval
         */
        public abstract double[] getCI();

        protected double getVariance(Stats stats) {
            return (this.sumOfSquares - Math.pow(stats.getSum(), 2)/ stats.getCount()) / (stats.getCount() - 1);
        }

        /**
         * Computes 95% confidence interval from current state of this instance.
         * @return [0] - lower bound, [1] upper bound of 95% confidence interval
         */
        protected double[] getCI(Stats stats) {
            if (stats.getCount() < 30)
                throw new RuntimeException("Cannot compute confidence interval for only "+stats.getCount()+ " samples."
                        + " There must be at least 30 samples.");
            double mean = stats.getMean();
            double h = this.getHalfWidthCI();
            return new double[]{mean - h, mean + h};
        }

        /**
         * @return half-width of 95% confidence interval.
         */
        protected double getHalfWidthCI(Stats stats) {
            if (stats.getCount() < 30)
                throw new RuntimeException("Cannot compute confidence interval for only "+stats.getCount()+ " samples." +
                        " There must be at least 30 samples.");
            return (this.getStdDev() * T_ALFA) / Math.sqrt(stats.getCount());
        }

        @Override
        public String toString() {
            return String.format("CI: * sum^2=%.3f\n  * var=%.3f" + "\n  * stdev=%.3f\n  * halfWidth=%.3f",
                    this.sumOfSquares, this.getVariance(), this.getStdDev(), this.getHalfWidthCI());
        }
    }

//  -   -   -   -   -   -   -   C I  f o r  A AVG-   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static class ArithmeticCI extends ConfidenceInterval {
        private ArithmeticAvg stats;

        public ArithmeticCI(ArithmeticAvg stats) {
            super();
            if (stats == null)
                throw new IllegalArgumentException("Stats not provided");
            this.stats = stats;
        }

        /**
         * Adds new sample to statistics from which is confidence interval computed.
         * @param value
         */
        public void addSample(double value) {
            this.stats.addSample(value);
            this.sumOfSquares += Math.pow(value, 2);
        }

        @Override
        public double getVariance() {
            return super.getVariance(this.stats);
        }

        @Override
        public double getHalfWidthCI() {
            return super.getHalfWidthCI(this.stats);
        }

        @Override
        public double[] getCI() {
            return super.getCI(this.stats);
        }
    }

//  -   -   -   -   -   -   -   C I  f o r  W AVG-   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static class WeightedCI extends ConfidenceInterval {
        private WeightedAvg stats;

        public WeightedCI(WeightedAvg stats) {
            super();
            if (stats == null)
                throw new IllegalArgumentException("Stats not provided");
            this.stats = stats;
        }

        public void addSample(double value, double weight) {
            this.stats.addSample(value, weight);
            this.sumOfSquares += Math.pow(value * weight, 2);
        }

        @Override
        public double getVariance() {
            return super.getVariance(this.stats);
        }

        @Override
        public double getHalfWidthCI() {
            return super.getHalfWidthCI(this.stats);
        }

        @Override
        public double[] getCI() {
            return super.getCI(this.stats);
        }
    }

    public static void main(String[] args) {
        Stats.ArithmeticAvg avgStats = new ArithmeticAvg();
        Stats.WeightedAvg wavgStats = new WeightedAvg();
        Generator rndS = new DiscreteUniformRnd(120,789);
        Generator rndW = new DiscreteUniformRnd(1,10);
        for (int i = 0; i < 1000; i++) {
            double sample = rndS.sample();
            double weight = rndW.sample();
            avgStats.addSample(sample);
            wavgStats.addSample(sample, weight);
        }
        System.out.println(avgStats);
        System.out.println(wavgStats);
    }

}
