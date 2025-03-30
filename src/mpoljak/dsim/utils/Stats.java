package mpoljak.dsim.utils;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.DiscreteUniformRnd;

import java.util.Arrays;

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
//  -   -   -   -   -   -   -   ARI WAVG -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
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
//  -   -   -   -   -   -   -   ARI WAVG -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
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
    public static class ConfidenceInterval extends ArithmeticAvg {
        private static final double T_ALFA = 1.96 ; // from 30 samples
        private double sumOfSquares;

        public ConfidenceInterval() {
            super();
            this.sumOfSquares = 0;
        }

        /**
         * Adds new sample to statistics from which is confidence interval computed.
         * @param value
         */
        @Override
        public void addSample(double value) {
            super.addSample(value);
            this.sumOfSquares += Math.pow(value, 2);
        }

    /**
         * @return standard deviation
         */
        public double getStdDev() {
                return Math.sqrt(this.getVariance());
        }


        public double getVariance() {
            return (this.sumOfSquares - Math.pow(this.sum, 2)/ this.count) / (this.count - 1);
        }

        /**
         * Computes 95% confidence interval from current state of this instance.
         * @return [0] - lower bound, [1] upper bound of 95% confidence interval
         */
        public double[] getCI() {
            if (this.count < 30)
                throw new RuntimeException("Cannot compute confidence interval for only "+this.count+ " samples."
                        + " There must be at least 30 samples.");
            double mean = this.getMean();
            double h = this.getHalfWidthCI();
            return new double[]{mean - h, mean + h};
        }

        /**
         * @return half-width of 95% confidence interval.
         */
        protected double getHalfWidthCI() {
            if (this.count < 30)
                throw new RuntimeException("Cannot compute confidence interval for only "+this.count+ " samples." +
                        " There must be at least 30 samples.");
            return (this.getStdDev() * T_ALFA) / Math.sqrt(this.count);
        }

        @Override
        public String toString() {
            return String.format("CI:\n * sum^2=%.3f\n  * var=%.3f" + "\n  * stdev=%.3f\n  * halfWidth=%.3f",
                    this.sumOfSquares, this.getVariance(), this.getStdDev(), this.getHalfWidthCI());
        }
    }

    public static void main(String[] args) {
        Stats.ArithmeticAvg avgStats = new ArithmeticAvg();
        Stats.WeightedAvg wavgStats = new WeightedAvg();
        Stats.ConfidenceInterval ci = new ConfidenceInterval();

        Generator rndS = new DiscreteUniformRnd(120,789);
        Generator rndW = new DiscreteUniformRnd(1,10);

        for (int i = 0; i < 1000; i++) {
            double sample = rndS.sample();
            double weight = rndW.sample();
            avgStats.addSample(sample);
            wavgStats.addSample(sample, weight);
            ci.addSample(sample);
        }

        System.out.println(avgStats);
        System.out.println(wavgStats);
        System.out.println(ci);
        double[] interval = ci.getCI();
        System.out.printf("Confidence interval: <%.03f   |%.03f|   %.03f>", interval[0], ci.getMean(), interval[1]);
    }

}
