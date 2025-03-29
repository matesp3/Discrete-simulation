package mpoljak.dsim.assignment_02.logic.sim;

import mpoljak.dsim.assignment_02.logic.events.DiscreteEvent;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.generators.ContinuosUniformRnd;
import mpoljak.dsim.generators.ExponentialRnd;
import mpoljak.dsim.generators.TriangularRnd;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FurnitureStore extends EventSim {
    private Queue<DiscreteEvent> queueA;
    private Queue<DiscreteEvent> queueB;
    private Queue<DiscreteEvent> queueC;
    // generators
    private final Generator rndOrderArrival;
    private final Generator rndOrderType;
    private final Generator rndFromStorageTransfer;
    private final Generator rndWoodPreparation;
    private final Generator rndDeskTransfer;
    // furniture creating - table
    private final Generator rndCarvingTable;
    private final Generator rndStainingTable;
    private final Generator rndAssemblingTable;
    // furniture creating - chair
    private final Generator rndCarvingChair;
    private final Generator rndStainingChair;
    private final Generator rndAssemblingChair;
    // furniture creating - wardrobe
    private final Generator rndCarvingWardrobe;
    private final Generator rndStainingWardrobe;
    private final Generator rndAssemblingWardrobe;
    private final Generator rndFitInstallWardrobe;

    public FurnitureStore(long replicationsCount, int amountA, int amountB, int amountC) {
        super(replicationsCount, 15, 358_560); // 60*24*249 = 358_560 [min]
        // simulation will be regards to minutes
        this.rndOrderArrival = new ExponentialRnd((2.0/60)); // lambda = (2 arrivals per 60 [min])
        this.rndOrderType = new ContinuosUniformRnd(0, 100); // generates percentages of probability of order's type
        this.rndFromStorageTransfer = new TriangularRnd(1, 8, 2); // (60s, 480s, 120s)
        this.rndWoodPreparation = new TriangularRnd(5, 15, 500.0/60.0); // (300s, 900s, 500s)
        this.rndDeskTransfer = new TriangularRnd(2, 500.0/60.0, 2.5); // (120s, 500s, 150s)

        this.rndCarvingTable = new ContinuosEmpiricalRnd(new double[] {10, 25}, new double[] {25, 50}, new double[] {0.6, 0.4});
        this.rndStainingTable = new ContinuosUniformRnd(200, 610);
        this.rndAssemblingTable = new ContinuosUniformRnd(30, 60);

        this.rndCarvingChair = new ContinuosUniformRnd(12, 16);
        this.rndStainingChair = new ContinuosUniformRnd(210, 540);
        this.rndAssemblingChair = new ContinuosUniformRnd(14, 24);

        this.rndCarvingWardrobe = new ContinuosUniformRnd(15, 80);
        this.rndStainingWardrobe = new ContinuosUniformRnd(600, 700);
        this.rndAssemblingWardrobe = new ContinuosUniformRnd(35, 75);
        this.rndFitInstallWardrobe = new ContinuosUniformRnd(15, 25);

        this.queueA = new ConcurrentLinkedQueue<>(); // https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
        this.queueB = new ConcurrentLinkedQueue<>(); // FIFO ordering based on the docs --^
        this.queueC = new ConcurrentLinkedQueue<>(); // methods of interest: add & poll
    }

    public static void main(String[] args) {

        Queue<Integer> fifo = new ConcurrentLinkedQueue<>();
        fifo.add(5);
        System.out.println(fifo);
        fifo.add(18);
        System.out.println(fifo);
        fifo.add(1);
        System.out.println(fifo);
        fifo.add(8);
        System.out.println(fifo);
        fifo.add(7);
        System.out.println(fifo);

        System.out.println("\n Polling..");
        final int l = fifo.size();
        for (int i = 0; i < l; i++) {
            Integer polled = fifo.poll();
            System.out.println("polled: " + polled);
        }
    }
}
