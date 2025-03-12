package mpoljak.dsim.intro;

import mpoljak.dsim.common.MCSimCore;

public class Main {
    public static void main(String[] args) {
        long repCount = 10_000_000;
        double d = 5;
        double l = 4;
        MCSimCore mc = new BuffonNeedle(repCount, l, d, null); // Monte carlo simulation
        mc.simulate();
        double prob = mc.getResult();
        double pi = (2 * l) / (d * prob);
        System.out.println("Probability of crossing: "+prob);
        System.out.println("Replications: "+repCount+"  | result PI: "+pi);

    }
}