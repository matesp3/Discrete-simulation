package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.events.OrderArrival;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.AfterEventResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdExpStats;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.generators.ContinuosUniformRnd;
import mpoljak.dsim.generators.ExponentialRnd;
import mpoljak.dsim.generators.TriangularRnd;
import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.Stats;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class FurnitureProductionSim extends EventSim {
    private static final double THRESHOLD_TABLE = 50;
    private static final double THRESHOLD_CHAIR = THRESHOLD_TABLE+15;
    private static final int PR_TOP = 0;
    private static final int PR_LOW = 5;
    // generators
    private final Generator rndOrderArrival;
    private final Generator rndOrderType;
    private final Generator rndFromStorageTransfer;
    private final Generator rndWoodPreparation;
    private final Generator rndDeskMoving;
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

    private final Generator rndDrying;
    // other logic
    private final Queue<FurnitureOrder> ordersA;
    private final Queue<FurnitureOrder> ordersB;
    private final Queue<FurnitureOrder> ordersCLowPr;
    private final Queue<FurnitureOrder> ordersCHighPr;
//    private final Queue<FurnitureOrder.OrderWithPriority> ordersC;
    private final DeskAllocation deskManager;
    private final Queue<Carpenter> freeA;
    private final Queue<Carpenter> freeB;
    private final Queue<Carpenter> freeC;
    private final int carpentersA;
    private final int carpentersB;
    private final int carpentersC;
    private int newOrderID; // ID of lastly assigned order
    private final Stats.ArithmeticAvg statOrderInSystemExp;
    private final Stats.ConfidenceInterval statOrderInSystemSim;

    public FurnitureProductionSim(long replicationsCount, int amountA, int amountB, int amountC, double timeInMinutes) {
        super(replicationsCount, 15, timeInMinutes); // 60min*8hod*249dni = 358_560 [min]
        // simulation will be regards to minutes
        this.rndOrderArrival = new ExponentialRnd((2.0/60)); // lambda = (2 arrivals per 60 [min])
        this.rndOrderType = new ContinuosUniformRnd(0, 100); // generates percentages of probability of order's type
        this.rndFromStorageTransfer = new TriangularRnd(1, 8, 2); // (60s, 480s, 120s)
        this.rndWoodPreparation = new TriangularRnd(5, 15, 500.0/60.0); // (300s, 900s, 500s)
        this.rndDeskMoving = new TriangularRnd(2, 500.0/60.0, 2.5); // (120s, 500s, 150s)

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

        this.rndDrying = new TriangularRnd(1, 200/60.0, 80/60.0);
//        this.ordersA = new ConcurrentLinkedQueue<>(); // https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
//        this.ordersB = new ConcurrentLinkedQueue<>(); // FIFO ordering based on the docs --^
//        this.ordersA = new PriorityBlockingQueue<>(100, new FurnitureOrder.OrderComparator());
//        this.ordersB = new PriorityBlockingQueue<>(100, new FurnitureOrder.OrderComparator());
//        this.ordersC = new PriorityBlockingQueue<>(100, new FurnitureOrder.PrOrderComparator()); // methods of interest: add & poll
//        this.ordersA = new PriorityQueue<>(100, new FurnitureOrder.OrderComparator());
//        this.ordersB = new PriorityQueue<>(100, new FurnitureOrder.OrderComparator());
        this.ordersA = new LinkedList<>(); // https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ConcurrentLinkedQueue.html
        this.ordersB = new LinkedList<>(); // FIFO ordering based on the docs --^
//        this.ordersC = new PriorityQueue<>(100, new FurnitureOrder.WardrobeComparator()); // methods of interest: add & poll
        this.ordersCLowPr = new LinkedList<>();
        this.ordersCHighPr = new LinkedList<>();
        this.deskManager = new DeskAllocation(amountA + amountB + amountC); // sum is enough - maximum occupancy

        Comparator<Carpenter> carpenterCmp = (o1, o2) -> Integer.compare(o1.getCarpenterId(), o2.getCarpenterId());
        this.freeA = new PriorityQueue<>(carpenterCmp);
        this.freeB = new PriorityQueue<>(carpenterCmp);
        this.freeC = new PriorityQueue<>(carpenterCmp);
        this.carpentersA = amountA;
        this.carpentersB = amountB;
        this.carpentersC = amountC;
        this.newOrderID = 1;

        this.statOrderInSystemExp = new Stats.ArithmeticAvg();
        this.statOrderInSystemSim = new Stats.ConfidenceInterval();
    }

    @Override
    protected void beforeSimulation() {
        super.beforeSimulation();
        // todo RESET SIM STATS
    }

    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        this.statOrderInSystemExp.reset();
        this.deskManager.freeAllDesks();
        this.ordersA.clear();
        this.ordersB.clear();
        this.ordersCLowPr.clear();
        this.ordersCHighPr.clear();
//        this.ordersC.clear();
        this.freeA.clear();
        this.freeB.clear();
        this.freeC.clear();
        this.createAndAddCarpenters(0, this.carpentersA-1, this.freeA, Carpenter.GROUP.A);
        this.createAndAddCarpenters(this.carpentersA, (this.carpentersA+this.carpentersB-1), this.freeB, Carpenter.GROUP.B);
        this.createAndAddCarpenters((this.carpentersA+this.carpentersB),
                (this.carpentersA+this.carpentersB+this.carpentersC-1), this.freeC, Carpenter.GROUP.C);
        this.newOrderID = 1;
        this.addToCalendar(new OrderArrival(0+this.nextUntilOrderArrivalDuration(), this, null));
    }

    /**
     * Assign unique order ID within single experiment run.
     * @return unique order ID as positive integer starting with value {@code 1}
     */
    public int assignOrderID() {
        return this.newOrderID++;
    }

    /**
     * @return {@code ID} of assigned desk, or {@code -1} if {@code requesterId} not provided.
     */
    public int assignFreeDesk(FurnitureOrder requesterId) {
        return this.deskManager.occupyDesk(requesterId);
    }

    /**
     * @param deskId desk to be set as free
     * @param requesterId ID to which was desk previously assigned
     */
    public void releaseDesk(int deskId, FurnitureOrder requesterId) {
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
                return;
            case STAINING:
                this.ordersCLowPr.add(order);
//                this.ordersC.add(new FurnitureOrder.OrderWithPriority(PR_LOW, order));
                return;
            case ASSEMBLING:
                this.ordersB.add(order);
                return;
            case FIT_INSTALLATION:
                this.ordersCHighPr.add(order);
//                this.ordersC.add(new FurnitureOrder.OrderWithPriority(PR_TOP, order));
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
     * @return generated time needed to move from (or to) the storage of wood
     */
    public double nextStorageAndHallMovingDuration() {
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
    public double nextDeskMovingDuration() {
        return this.rndDeskMoving.sample();
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

    public double nextDryingDuration() { return this.rndDrying.sample(); }

    /**
     * @return carpenter with the highest priority from {@code group} of free carpenters or {@code null} if no available
     */
    public Carpenter getFirstFreeCarpenter(Carpenter.GROUP group) {
        Queue<Carpenter> freeCarpenters = this.getRelevantCarpenterQueue(group);
        return freeCarpenters.poll(); // retrieves carpenter with the lowest ID
    }

    /**
     * Retrieves carpenter from wanted <code>deskID</code> if there's some free from group <code>group</code>.
     * If not, retrieves first other free carpenter from group <code>group</code>.
     * @param group
     * @param deskID
     * @return carpenter's instance of group <code>group</code> or {@code null} if no available
     */
    public Carpenter getFreeCarpenterWithPreference(Carpenter.GROUP group, int deskID) {
        Queue<Carpenter> freeCarpenters = this.getRelevantCarpenterQueue(group);
        Carpenter best = null;
        for (Carpenter c : freeCarpenters) {
            if (c.getCurrentDeskID() == deskID && (best == null || c.getCarpenterId() < best.getCarpenterId()))
                best = c;
        }
        if (best == null) // unable to find by the preference
            return this.getFirstFreeCarpenter(group);
        freeCarpenters.remove(best);
        return best;
    }

    /**
     * Enqueues carpenter who ended his task to its corresponding group of free carpenters.
     */
    public void returnCarpenter(Carpenter carpenter) {
        if (carpenter == null)
            return;
        this.getRelevantCarpenterQueue(carpenter.getGroup()).add(carpenter);
    }

    /**
     * @return Order with the highest priority to be processed by group {@code carpenterGroup} or {@code null} if
     * there's no waiting order for processing by group {@code carpenterGroup}
     */
    public FurnitureOrder getOrderForCarpenter(Carpenter.GROUP carpenterGroup) {
        switch (carpenterGroup) {
            case A:
                return this.ordersA.poll();
            case B:
                return this.ordersB.poll();
            case C:
                if (!this.ordersCHighPr.isEmpty())
                    return this.ordersCHighPr.poll();
                return this.ordersCLowPr.poll();
//                if (this.ordersC.peek() == null)
//                    return null;
//                return this.ordersC.poll().getOrder();
        }
        throw new IllegalArgumentException("Invalid carpenter group");
    }

    /**
     * @return {@code true} if doesn't exist any order that is waiting for processing by specified {@code group}
     */
    public boolean hasNotWaitingOrder(Carpenter.GROUP group) {
        if (group == Carpenter.GROUP.C)
            return this.ordersCLowPr.isEmpty() && this.ordersCHighPr.isEmpty();
//            return this.ordersC.isEmpty();
        return (group == Carpenter.GROUP.A ? this.ordersA : this.ordersB).isEmpty();
    }

    /**
     * @return whether is not any carpenter of {@code group} available for new order processing
     */
    public boolean hasNotAvailableCarpenter(Carpenter.GROUP group) {
        return this.getRelevantCarpenterQueue(group).isEmpty();
    }

    @Override
    protected void afterExperiment() {
        super.afterExperiment();
        this.statOrderInSystemSim.addSample(this.statOrderInSystemExp.getMean());
        double count = this.statOrderInSystemSim.getCount();
        if (count >= 30 && count%2000==0)
            System.out.println("Order duration in system: "+this.statOrderInSystemSim);
        if (count > 30) {
            FurnitProdExpStats res = new FurnitProdExpStats(this.getCurrentReplication());
            res.addResult(new StatResult("Order time in system", confIntToStr(this.statOrderInSystemSim.getHalfWidthCI() / 60.0
                    , this.statOrderInSystemSim.getMean() / 60.0), "[h]"));
            this.notifyDelegates(res);
        }
    }

    @Override
    protected void afterSimulation() {
        super.afterSimulation();
    }

    //    - -   -   -   -   -   -   S T A T I S T I C S  -   -   -   -   -   -   -   -   -
    public void receiveEventResults(AfterEventResults results) {
        this.notifyDelegates(results);
    }

    public void addOrderTimeInSystem(double duration) {
        this.statOrderInSystemExp.addSample(duration);
    }

    /**
     * @return queue of carpenters based on param <code>group</code>
     */
    private Queue<Carpenter> getRelevantCarpenterQueue(Carpenter.GROUP group) {
        return group == Carpenter.GROUP.A ? this.freeA : (group == Carpenter.GROUP.B ? this.freeB : this.freeC);
    }

    private void createAndAddCarpenters(int firstID, int lastID, Queue<Carpenter> freeCarpenters, Carpenter.GROUP group) {
        for (int id = firstID; id <= lastID; id++) {
            freeCarpenters.add(new Carpenter(group, id));
        }
    }

    private static String confIntToStr(double h, double mean) {
        return String.format("95%%: <%.2f     | %.02f |   %.02f>", mean-h, mean, mean+h);
    }
//    - -   -   -   -   -   -   - testing... ---v

    public static void main(String[] args) throws InterruptedException {
//        FurnitureStoreSim sim = new FurnitureStoreSim(1, 1, 2, 3);
//        Carpenter carp = sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
//        FurnitureOrder order = new FurnitureOrder(sim.assignOrderID(), 5.4, FurnitureOrder.Product.TABLE);
//        int deskID = sim.assignFreeDesk(order);
//        order.setDeskID(deskID);
//        sim.releaseDesk(deskID, order);
//        sim.returnCarpenter(carp); // ok

        Queue<FurnitureOrder.OrderWithPriority> ordersC =  new PriorityBlockingQueue<>(5, new FurnitureOrder.WardrobeComparator());
        ordersC.add(new FurnitureOrder.OrderWithPriority(PR_LOW, new FurnitureOrder(1, 1.0, FurnitureOrder.Product.CHAIR)));
    }
}
