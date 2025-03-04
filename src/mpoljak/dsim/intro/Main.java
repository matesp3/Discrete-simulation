package mpoljak.dsim.intro;

import mpoljak.dsim.common.MCSimCore;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        long repCount = 10_000_000;
        double d = 5;
        double l = 4;
        MCSimCore mc = new BuffonNeedle(new Random(), repCount, l, d ); // Monte carlo simulation
        double prob = mc.run(repCount);
        double pi = (2 * l) / (d * prob);
        System.out.println("Probability of crossing: "+prob);
        System.out.println("Replications: "+repCount+"  | result PI: "+pi);

        Random rnd = new Random();
        double minVal = 10;
        double maxVal = 70;
        double percent = rnd.nextDouble() * (maxVal - minVal) + minVal;
        if (rnd.nextDouble()*100 <= percent)
            System.out.println("dodavatel doda suciastky");
        else
            System.out.println("dodavatel NEdoda suciastky");
    }
}