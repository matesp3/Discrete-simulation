package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.generators.ContinuosUniformRnd;
import mpoljak.dsim.generators.ExponentialRnd;
import mpoljak.dsim.generators.TriangularRnd;
import mpoljak.dsim.utils.DoubleComp;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class FurnitureStoreSim extends EventSim {
    private static final double THRESHOLD_TABLE = 50;
    private static final double THRESHOLD_CHAIR = THRESHOLD_TABLE+15;
    private static final int PR_TOP = 0;
    private static final int PR_LOW = 5;
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
    // other logic
    private final Queue<FurnitureOrder> ordersA;
    private final Queue<FurnitureOrder> ordersB;
    private final Queue<FurnitureOrder.OrderWithPriority> ordersC;
    private final DeskAllocation deskManager;
    private final Queue<Carpenter> freeA;
    private final Queue<Carpenter> freeB;
    private final Queue<Carpenter> freeC;

    public FurnitureStoreSim(long replicationsCount, int amountA, int amountB, int amountC) {
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

        this.ordersA = new ConcurrentLinkedQueue<>(); // https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
        this.ordersB = new ConcurrentLinkedQueue<>(); // FIFO ordering based on the docs --^
        this.ordersC = new PriorityBlockingQueue<>(); // methods of interest: add & poll
        this.deskManager = new DeskAllocation(amountA + amountB + amountC); // sum is enough - maximum occupancy

        Comparator<Carpenter> carpenterCmp = (o1, o2) -> Integer.compare(o1.getCarpenterId(), o2.getCarpenterId());
        this.freeA = new PriorityQueue<>(carpenterCmp);
        this.freeB = new PriorityQueue<>(carpenterCmp);
        this.freeC = new PriorityQueue<>(carpenterCmp);
        this.createAndAddCarpenters(0, amountA-1, this.freeA, Carpenter.GROUP.A);
        this.createAndAddCarpenters(amountA, amountA+amountB-1, this.freeB, Carpenter.GROUP.B);
        this.createAndAddCarpenters(amountA+amountB, amountA+amountB+amountC-1, this.freeC, Carpenter.GROUP.C);
    }

    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        this.deskManager.freeAllDesks();
    }

    /**
     * @return <code>ID</code> of assigned desk, or <code>-1</code> if there was no free desk to occupy or
     * <code>requesterId</code> not provided.
     */
    public int assignFreeDesk(Carpenter requesterId) {
        return this.deskManager.occupyDesk(requesterId);
    }

    /**
     * @param deskId desk to be set as free
     * @param requesterId ID to which was desk previously assigned
     */
    public void releaseDesk(int deskId, Carpenter requesterId) {
        this.deskManager.setDeskFree(deskId, requesterId);
    }


    /**
     * Based on <code>order</code>'s next technological step, enqueues order to proper queue of waiting for processing
     * by carpenter.
     * @param order to be enqueued
     */
    public void enqueueForNextProcessing(FurnitureOrder order) {
        switch (order.getNextTechStep()) {
            case WOOD_PREPARATION:
            case CARVING:
                this.ordersA.add(order);
                break;
            case STAINING:
                this.ordersC.add(new FurnitureOrder.OrderWithPriority(PR_LOW, order));
                break;
            case ASSEMBLING:
                this.ordersB.add(order);
                break;
            case FIT_INSTALLATION:
                this.ordersC.add(new FurnitureOrder.OrderWithPriority(PR_TOP, order));
        }
    }

    /**
     * @return generated product type of new order
     */
    public FurnitureOrder.Product nextProductType() {
        double generated = this.rndOrderType.sample();
        return  (DoubleComp.compare(generated, THRESHOLD_TABLE) == -1)
                ? FurnitureOrder.Product.TABLE : ((DoubleComp.compare(generated, THRESHOLD_CHAIR) == -1)
                ? FurnitureOrder.Product.CHAIR : FurnitureOrder.Product.WARDROBE);
    }

    /**
     * @return generated time needed to wait until new order comes into the system.
     */
    public double nextUntilOrderArrivalDuration() {
        return this.rndOrderArrival.sample();
    }

    /**
     * @return generated time needed to move from or to the storage of wood
     */
    public double nextStorageAndHallTransferDuration() {
        return this.rndFromStorageTransfer.sample();
    }

    /**
     * @return generated time needed to execute wood preparation by carpenter of group A
     */
    public double nextWoodPreparationDuration() {
        return this.rndWoodPreparation.sample();
    }

    /**
     * @return generated time needed to move from one desk to another desk
     */
    public double nextDeskTransferDuration() {
        return this.rndDeskTransfer.sample();
    }

    /**
     * @return generated time needed to execute carving of <code>furnitureType</code> product
     */
    public double nextCarvingDuration(FurnitureOrder.Product furnitureType) {
        switch (furnitureType) {
            case CHAIR:
                return this.rndCarvingChair.sample();
            case TABLE:
                return this.rndCarvingTable.sample();
            case WARDROBE:
                return this.rndCarvingWardrobe.sample();
        }
        return -1;
    }

    /**
     * @return generated time needed to execute staining of <code>furnitureType</code> product
     */
    public double nextStainingDuration(FurnitureOrder.Product furnitureType) {
        switch (furnitureType) {
            case CHAIR:
                return this.rndStainingChair.sample();
            case TABLE:
                return this.rndStainingTable.sample();
            case WARDROBE:
                return this.rndStainingWardrobe.sample();
        }
        return -1;
    }

    /**
     * @return generated time needed to execute assembling of <code>furnitureType</code> product
     */
    public double nextAssemblingDuration(FurnitureOrder.Product furnitureType) {
        switch (furnitureType) {
            case CHAIR:
                return this.rndAssemblingChair.sample();
            case TABLE:
                return this.rndAssemblingTable.sample();
            case WARDROBE:
                return this.rndAssemblingWardrobe.sample();
        }
        return -1;
    }

    /**
     * @return generated time needed to execute fit installation of wardrobe
     */
    public double nextFitInstallationDuration() {
        return this.rndFitInstallWardrobe.sample();
    }

    /**
     * @return carpenter with the highest priority from {@code group} of free carpenters
     */
    public Carpenter getFirstFreeCarpenter(Carpenter.GROUP group) {
        Queue<Carpenter> freeCarpenters = this.getRelevantQueue(group);
        return freeCarpenters.poll(); // retrieves carpenter with the lowest ID
    }

    /**
     * Retrieves carpenter from wanted <code>deskID</code> if there's some free from group <code>group</code>.
     * If not, retrieves first other free carpenter from group <code>group</code>.
     * @param group
     * @param deskID
     * @return carpenter's instance of group <code>group</code>
     */
    public Carpenter getFreeCarpenterWithPreference(Carpenter.GROUP group, int deskID) {
        Queue<Carpenter> freeCarpenters = this.getRelevantQueue(group);
        Carpenter best = null;
        for (Carpenter c : freeCarpenters) {
            if (c.getDeskID() == deskID && (best == null || c.getCarpenterId() < best.getCarpenterId()))
                best = c;
        }
        if (best == null) // unable to find by the preference
            return this.getFirstFreeCarpenter(group);
        freeCarpenters.remove(best);
        return best;
    }

    /**
     * Enqueues carpenter who ended his task to its corresponding group.
     */
    public void returnCarpenter(Carpenter carpenter) {
        if (carpenter == null)
            return;
        this.getRelevantQueue(carpenter.getGroup()).add(carpenter);
    }

    /**
     * @return Order with the highest priority to be processed by group <code>carpenterGroup</code>
     */
    public FurnitureOrder getOrderForCarpenter(Carpenter.GROUP carpenterGroup) {
        switch (carpenterGroup) {
            case A:
                return this.ordersA.poll();
            case B:
                return this.ordersB.poll();
            case C:
                if (this.ordersC.peek() == null)
                    return null;
                return this.ordersC.poll().getOrder();
        }
        return null;
    }

    /**
     * @return queue of carpenters based on param <code>group</code>
     */
    private Queue<Carpenter> getRelevantQueue(Carpenter.GROUP group) {
        return group == Carpenter.GROUP.A ? this.freeA : (group == Carpenter.GROUP.B ? this.freeB : this.freeC);
    }

    private void createAndAddCarpenters(int firstID, int lastID, Queue<Carpenter> freeCarpenters, Carpenter.GROUP group) {
        for (int id = firstID; id <= lastID; id++) {
            freeCarpenters.add(new Carpenter(group, id));
        }
    }

//    - -   -   -   -   -   -   - testing... ---v
    public static void main(String[] args) throws InterruptedException {
        FurnitureStoreSim sim = new FurnitureStoreSim(1, 1, 2, 3);
        Carpenter carp = sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
        int deskID = sim.assignFreeDesk(carp);
        sim.releaseDesk(deskID, carp);
        sim.returnCarpenter(carp); // ok

    }
}
