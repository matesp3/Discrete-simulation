package mpoljak.dsim.intro;

import mpoljak.dsim.common.MCSimCore;

public class Main {
    public static void main(String[] args) {
        long repCount = 10_000_000;
        double d = 5;
        double l = 4;
        MCSimCore mc = new BuffonNeedle(l, d ); // Monte carlo simulation
        double prob = mc.run(repCount);
        double pi = (2 * l) / (d * prob);
        System.out.println("Probability of crossing: "+prob);
        System.out.println("Replications: "+repCount+"  | result PI: "+pi);

        //todo kniznica na robenie grafov: JFreeChart - naucit sa robit vykreslovanie
        //todo 2: v stredu sa zverejni zadanie, tak si ho poriadne precitat, aby som na cviku bol nan pripraveny, lebo na dalsi tyzden je kontrola
    }
}