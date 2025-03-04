package mpoljak.dsim.assignment_01.generators;

import mpoljak.dsim.common.Generator;

import java.util.Random;

public class ContinuosEmpiricalRnd extends Generator {
    protected Random rand;
    /**
     * @param seedGen generator that is used to initialize all instance's inner generators with 'proper' seed
     */
    public ContinuosEmpiricalRnd(Random seedGen) {
        super(seedGen);
        this.rand = new Random(seedGen.nextLong());
        // TODO OTESTOVAT GENERATORY EMPIRICKE - V EXCELI VLOZIT 1000 VZORIEK A Z TOHO URCIT ROZDELENIE
    }


    @Override
    public double sample() {
        return 0;
    }
}
