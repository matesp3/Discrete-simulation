package mpoljak.dsim.utils;

import java.util.Random;

// multithreaded singleton
public class SeedGen {
    private static volatile SeedGen instance;

    private final Random rand;

    protected SeedGen() {
        this.rand = new Random(); // in case of debugging, just set seed
    }

    public static SeedGen getInstance() {
        if (instance == null) {
            synchronized (SeedGen.class) {
                if (instance == null) {
                    instance = new SeedGen();
                }
            }
        }
        return instance;
    }

    public long nextSeed() {
        return this.rand.nextLong();
    }
}
