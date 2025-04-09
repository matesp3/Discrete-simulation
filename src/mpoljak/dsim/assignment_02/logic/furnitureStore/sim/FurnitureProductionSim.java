package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.events.OrderArrival;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.*;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.common.SimCommand;
import mpoljak.dsim.generators.ContinuosEmpiricalRnd;
import mpoljak.dsim.generators.ContinuosUniformRnd;
import mpoljak.dsim.generators.ExponentialRnd;
import mpoljak.dsim.generators.TriangularRnd;
import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.Stats;

import java.util.*;

public class FurnitureProductionSim extends EventSim {
    private static final double THRESHOLD_TABLE = 50;
    private static final double THRESHOLD_CHAIR = THRESHOLD_TABLE+15;
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
    private final Carpenter[] groupA;
    private final Carpenter[] groupB;
    private final Carpenter[] groupC;
    private final DeskAllocation deskManager;
    private final Queue<Carpenter> freeA;
    private final Queue<Carpenter> freeB;
    private final Queue<Carpenter> freeC;
    // statistics
    private int newOrderID; // ID of lastly assigned order
    private final Stats.ArithmeticAvg statExpOrderTimeInSystem;
    private final Stats.ConfidenceInterval statAllOrderTimeInSystem;

    private final Stats.TimeWeightedAvg statExpWaitingCount;
    private final Stats.TimeWeightedAvg statExpStainingCount;
    private final Stats.TimeWeightedAvg statExpAssemblingCount;
    private final Stats.TimeWeightedAvg statExpFitInstCount;
    private final Stats.ConfidenceInterval statAllWaitingCount;
    private final Stats.ConfidenceInterval statAllStainingCount;
    private final Stats.ConfidenceInterval statAllAssemblingCount;
    private final Stats.ConfidenceInterval statAllFitInstCount;

    private final Stats.ConfidenceInterval statAllUtilizationA;
    private final Stats.ConfidenceInterval statAllUtilizationB;
    private final Stats.ConfidenceInterval statAllUtilizationC;

    private final Stats.ArithmeticAvg statExpInWaitingQueueTime;
    private final Stats.ArithmeticAvg statExpInStainingQueueTime;
    private final Stats.ArithmeticAvg statExpInAssemblingQueueTime;
    private final Stats.ArithmeticAvg statExpInFitInstQueueTime;
    private final Stats.ConfidenceInterval statAllInWaitingQueueTime;
    private final Stats.ConfidenceInterval statAllInStainingQueueTime;
    private final Stats.ConfidenceInterval statAllInAssemblingQueueTime;
    private final Stats.ConfidenceInterval statAllInFitInstQueueTime;
    // sim results
    private final FurnitProdEventResults eventResults;

    public FurnitureProductionSim(long replicationsCount, int amountA, int amountB, int amountC, double timeInMinutes) {
        super(replicationsCount, 15, timeInMinutes); // 60min*8hod*249dni = 358_560 [min]
        // simulation time unit will be computed in [minute] -> make sure duration generators are parametrized in minutes
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
        // orders
        this.rndDrying = new TriangularRnd(1, 200/60.0, 80/60.0);
        this.ordersA = new LinkedList<>();
        this.ordersB = new LinkedList<>(); // FIFO ordering based on the docs
        this.ordersCLowPr = new LinkedList<>();
        this.ordersCHighPr = new LinkedList<>();
        this.deskManager = new DeskAllocation(amountA + amountB + amountC); // sum is enough - maximum occupancy
        // carpenters
        this.groupA = new Carpenter[amountA];
        this.createAndSetCarpenters(1, this.groupA, Carpenter.GROUP.A);
        this.groupB = new Carpenter[amountB];
        this.createAndSetCarpenters(this.groupA.length+1, this.groupB, Carpenter.GROUP.B);
        this.groupC = new Carpenter[amountC];
        this.createAndSetCarpenters(this.groupA.length+this.groupB.length+1, this.groupC, Carpenter.GROUP.C);
        Comparator<Carpenter> carpenterCmp = (o1, o2) -> Integer.compare(o1.getCarpenterId(), o2.getCarpenterId());
        this.freeA = new PriorityQueue<>(amountA, carpenterCmp);
        this.freeB = new PriorityQueue<>(amountB, carpenterCmp);
        this.freeC = new PriorityQueue<>(amountC, carpenterCmp);
        this.newOrderID = 1;
        // stats
        this.statExpOrderTimeInSystem = new Stats.ArithmeticAvg();
        this.statAllOrderTimeInSystem = new Stats.ConfidenceInterval();

        this.statExpWaitingCount = new Stats.TimeWeightedAvg();
        this.statExpStainingCount = new Stats.TimeWeightedAvg();
        this.statExpAssemblingCount = new Stats.TimeWeightedAvg();
        this.statExpFitInstCount = new Stats.TimeWeightedAvg();
        this.statAllWaitingCount = new Stats.ConfidenceInterval();
        this.statAllStainingCount = new Stats.ConfidenceInterval();
        this.statAllAssemblingCount = new Stats.ConfidenceInterval();
        this.statAllFitInstCount = new Stats.ConfidenceInterval();

        this.statAllUtilizationA = new Stats.ConfidenceInterval();
        this.statAllUtilizationB = new Stats.ConfidenceInterval();
        this.statAllUtilizationC = new Stats.ConfidenceInterval();

        this.statExpInWaitingQueueTime = new Stats.ArithmeticAvg();
        this.statExpInStainingQueueTime = new Stats.ArithmeticAvg();
        this.statExpInAssemblingQueueTime = new Stats.ArithmeticAvg();
        this.statExpInFitInstQueueTime = new Stats.ArithmeticAvg();
        this.statAllInWaitingQueueTime = new Stats.ConfidenceInterval();
        this.statAllInStainingQueueTime = new Stats.ConfidenceInterval();
        this.statAllInAssemblingQueueTime = new Stats.ConfidenceInterval();
        this.statAllInFitInstQueueTime = new Stats.ConfidenceInterval();
        // results information
        this.eventResults = new FurnitProdEventResults(0, 0, amountA, amountB, amountC);
        SimCommand resultsPrepCmd = new SimCommand(SimCommand.SimCommandType.CUSTOM) {
            @Override
            public void invoke() {
                eventResults.setSimTime(getSimTime());
                eventResults.setExperimentNum(getCurrentReplication());
                eventResults.setModelsCarpentersA(groupA);
                eventResults.setModelsCarpentersB(groupB);
                eventResults.setModelsCarpentersC(groupC);
                eventResults.setOrdersA(ordersA);
                eventResults.setOrdersB(ordersB);
                eventResults.setOrdersCLow(ordersCLowPr);
                eventResults.setOrdersCHigh(ordersCHighPr);
                eventResults.clearPrevStats();
                eventResults.addStat(new StatResult("Time-WAVG orders count-Waiting", String.format("%.5f",statExpWaitingCount.getMean()), "[qty]"));
                eventResults.addStat(new StatResult("Time-WAVG orders count-Assembling", String.format("%.5f",statExpAssemblingCount.getMean()), "[qty]"));
                eventResults.addStat(new StatResult("Time-WAVG orders count-Staining", String.format("%.5f",statExpStainingCount.getMean()), "[qty]"));
                eventResults.addStat(new StatResult("Time-WAVG orders count-Fit inst.", String.format("%.5f",statExpFitInstCount.getMean()), "[qty]"));
                eventResults.addStat(new StatResult("AVG order's time in system", String.format("%.5f",statExpOrderTimeInSystem.getMean()/60.0), "[h]"));
                eventResults.addStat(new StatResult("Utilization of group A", String.format("%.5f",getUtilization(groupA, getSimTime())*100), "[%]"));
                eventResults.addStat(new StatResult("Utilization of group B", String.format("%.5f",getUtilization(groupB, getSimTime())*100), "[%]"));
                eventResults.addStat(new StatResult("Utilization of group C", String.format("%.5f",getUtilization(groupC, getSimTime())*100), "[%]"));
                eventResults.addStat(new StatResult("AVG Time in Waiting queue", String.format("%.5f",statExpInWaitingQueueTime.getMean()/60.0), "[h]"));
                eventResults.addStat(new StatResult("AVG Time in Staining queue", String.format("%.5f",statExpInStainingQueueTime.getMean()/60.0), "[h]"));
                eventResults.addStat(new StatResult("AVG Time in Assembling queue", String.format("%.5f",statExpInAssemblingQueueTime.getMean()/60.0), "[h]"));
                eventResults.addStat(new StatResult("AVG Time in Fit inst. queue", String.format("%.5f",statExpInFitInstQueueTime.getMean()/60.0), "[h]"));
                eventResults.addStat(new StatResult("Allocated desks count", String.valueOf(deskManager.getAllocatedDesksCount()), "[qty]"));
            }
        };
        this.eventResults.setResultsPreparationCommand(resultsPrepCmd);
    }

    @Override
    protected void beforeSimulation() {
        super.beforeSimulation();

        statAllWaitingCount.reset();
        statAllStainingCount.reset();
        statAllAssemblingCount.reset();
        statAllFitInstCount.reset();

        statAllOrderTimeInSystem.reset();

        statAllUtilizationA.reset();
        statAllUtilizationB.reset();
        statAllUtilizationC.reset();

        statAllInWaitingQueueTime.reset();
        statAllInStainingQueueTime.reset();
        statAllInAssemblingQueueTime.reset();
        statAllInFitInstQueueTime.reset();

        this.notifyDelegates(this.eventResults);
    }

    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        this.deskManager.freeAllDesks();
        this.ordersA.clear();
        this.ordersB.clear();
        this.ordersCLowPr.clear();
        this.ordersCHighPr.clear();
        this.resetAndFillQueue(this.groupA, this.freeA);
        this.resetAndFillQueue(this.groupB, this.freeB);
        this.resetAndFillQueue(this.groupC, this.freeC);
        this.newOrderID = 1;
        this.addToCalendar(new OrderArrival(0+this.nextUntilOrderArrivalDuration(), this, null));
        // stats
        this.statExpOrderTimeInSystem.reset();

        this.statExpWaitingCount.reset();
        this.statExpStainingCount.reset();
        this.statExpAssemblingCount.reset();
        this.statExpFitInstCount.reset();

        this.statExpInWaitingQueueTime.reset();
        this.statExpInStainingQueueTime.reset();
        this.statExpInAssemblingQueueTime.reset();
        this.statExpInFitInstQueueTime.reset();
    }

    @Override
    protected void afterEventExecution() {
        this.notifyDelegates(this.eventResults);
    }

    @Override
    protected void afterExperiment() {
        super.afterExperiment();
        this.statAllWaitingCount.addSample(this.statExpWaitingCount.getMean());
        this.statAllStainingCount.addSample(this.statExpStainingCount.getMean());
        this.statAllAssemblingCount.addSample(this.statExpAssemblingCount.getMean());
        this.statAllFitInstCount.addSample(this.statExpFitInstCount.getMean());

        this.statAllOrderTimeInSystem.addSample(this.statExpOrderTimeInSystem.getMean());

        this.statAllUtilizationA.addSample(getUtilization(this.groupA, this.getSimTime()));
        this.statAllUtilizationB.addSample(getUtilization(this.groupB, this.getSimTime()));
        this.statAllUtilizationC.addSample(getUtilization(this.groupC, this.getSimTime()));

        this.statAllInWaitingQueueTime.addSample(this.statExpInWaitingQueueTime.getMean());
        this.statAllInStainingQueueTime.addSample(this.statExpInStainingQueueTime.getMean());
        this.statAllInAssemblingQueueTime.addSample(this.statExpInAssemblingQueueTime.getMean());
        this.statAllInFitInstQueueTime.addSample(this.statExpInFitInstQueueTime.getMean());

        double count = this.statAllOrderTimeInSystem.getCount();
        if (count >= 30 && count%2000==0)
            System.out.println("Order duration in system: "+this.statAllOrderTimeInSystem);
        if (count > 30) {
            FurnitProdExpStats res = new FurnitProdExpStats(this.getCurrentReplication());
            res.addResult(new StatResult("Orders count-Waiting", confIntToStr(statAllWaitingCount.getHalfWidthCI(),
                    statAllWaitingCount.getMean(), 5, 1),"[qty]"));
            res.addResult(new StatResult("Orders count-Assembling", confIntToStr(statAllAssemblingCount.getHalfWidthCI(),
                    statAllAssemblingCount.getMean(), 5, 1),"[qty]"));
            res.addResult(new StatResult("Orders count-Staining", confIntToStr(statAllStainingCount.getHalfWidthCI(),
                    statAllStainingCount.getMean(), 5, 1),"[qty]"));
            res.addResult(new StatResult("Orders count-Fit inst.", confIntToStr(statAllFitInstCount.getHalfWidthCI(),
                    statAllFitInstCount.getMean(), 5, 1),"[qty]"));
            res.addResult(new StatResult("Utilization of group A", confIntToStr(statAllUtilizationA.getHalfWidthCI(),
                    statAllUtilizationA.getMean(), 5, 0.01), "[%]"));
            res.addResult(new StatResult("Utilization of group B", confIntToStr(statAllUtilizationB.getHalfWidthCI(),
                    statAllUtilizationB.getMean(), 5, 0.01), "[%]"));
            res.addResult(new StatResult("Utilization of group C", confIntToStr(statAllUtilizationC.getHalfWidthCI(),
                    statAllUtilizationC.getMean(), 5, 0.01), "[%]"));
            res.addResult(new StatResult("Time in Waiting queue", confIntToStr(statAllInWaitingQueueTime.getHalfWidthCI(),
                    statAllInWaitingQueueTime.getMean(), 5, 60), "[h]"));
            res.addResult(new StatResult("Time in Staining queue", confIntToStr(statAllInStainingQueueTime.getHalfWidthCI(),
                    statAllInStainingQueueTime.getMean(), 5, 60), "[h]"));
            res.addResult(new StatResult("Time in Assembling queue", confIntToStr(statAllInAssemblingQueueTime.getHalfWidthCI(),
                    statAllInAssemblingQueueTime.getMean(), 5, 60), "[h]"));
            res.addResult(new StatResult("Time in Fit inst. queue", confIntToStr(statAllInFitInstQueueTime.getHalfWidthCI(),
                    statAllInFitInstQueueTime.getMean(), 5, 60), "[h]"));
            res.addResult(new StatResult("Order time in system", confIntToStr(statAllOrderTimeInSystem.getHalfWidthCI(),
                    statAllOrderTimeInSystem.getMean(), 5, 60), "[h]"));
            res.setOrderTimeInSystem(statAllOrderTimeInSystem.getMean(), statAllOrderTimeInSystem.getHalfWidthCI());
            this.notifyDelegates(res);
        }
    }

    @Override
    protected void afterSimulation() {
        super.afterSimulation();
        OtherEventInfo info = new OtherEventInfo(this.getCurrentReplication(), "Sim:ended");
        this.notifyDelegates(info);
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
        order.setWaitingBT(this.getSimTime());
        switch (order.getStep()) {
            case WOOD_PREPARATION:
            case CARVING:
                this.ordersA.add(order);
                this.statExpWaitingCount.addSample(this.ordersA.size(), this.getSimTime());
                return;
            case STAINING:
                this.ordersCLowPr.add(order);
                this.statExpStainingCount.addSample(this.ordersCLowPr.size(), this.getSimTime());
                return;
            case ASSEMBLING:
                this.ordersB.add(order);
                this.statExpAssemblingCount.addSample(this.ordersB.size(), this.getSimTime());
                return;
            case FIT_INSTALLATION:
                this.ordersCHighPr.add(order);
                this.statExpFitInstCount.addSample(this.ordersCHighPr.size(), this.getSimTime());
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
        return this.getRelevantCarpenterQueue(group).poll(); // retrieves carpenter with the lowest ID
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
        FurnitureOrder order = null;
        switch (carpenterGroup) {
            case A:
                order = this.ordersA.poll();
                this.statExpWaitingCount.addSample(this.ordersA.size(), this.getSimTime());
                this.statExpInWaitingQueueTime.addSample(this.getSimTime() - order.getWaitingBT());
                break;
            case B:
                order = this.ordersB.poll();
                this.statExpAssemblingCount.addSample(this.ordersB.size(), this.getSimTime());
                this.statExpInAssemblingQueueTime.addSample(this.getSimTime() - order.getWaitingBT());
                break;
            case C:
                if (!this.ordersCHighPr.isEmpty()) {
                    order = this.ordersCHighPr.poll();
                    this.statExpFitInstCount.addSample(this.ordersCHighPr.size(), this.getSimTime());
                    this.statExpInFitInstQueueTime.addSample(this.getSimTime() - order.getWaitingBT());
                    break;
                }
                order = this.ordersCLowPr.poll();
                this.statExpStainingCount.addSample(this.ordersCLowPr.size(), this.getSimTime());
                this.statExpInStainingQueueTime.addSample(this.getSimTime() - order.getWaitingBT());
                break;
            default:
                throw new IllegalArgumentException("Invalid carpenter group");
        }
        return order;
    }

    /**
     * @return {@code true} if doesn't exist any order that is waiting for processing by specified {@code group}
     */
    public boolean hasNotWaitingOrder(Carpenter.GROUP group) {
        if (group == Carpenter.GROUP.C)
            return this.ordersCLowPr.isEmpty() && this.ordersCHighPr.isEmpty();
        return (group == Carpenter.GROUP.A ? this.ordersA : this.ordersB).isEmpty();
    }

    /**
     * @return whether is not any carpenter of {@code group} available for new order processing
     */
    public boolean hasNotAvailableCarpenter(Carpenter.GROUP group) {
        return this.getRelevantCarpenterQueue(group).isEmpty();
    }

    //    - -   -   -   -   -   -   S T A T I S T I C S  -   -   -   -   -   -   -   -   -
    public void receiveEventResults(AfterEventResults results) {
        this.notifyDelegates(results);
    }

    public void receiveCompletedOrder(FurnitureOrder completed) {
        this.statExpOrderTimeInSystem.addSample(completed.getOverallTime());
    }

    /**
     * @return queue of carpenters based on param <code>group</code>
     */
    private Queue<Carpenter> getRelevantCarpenterQueue(Carpenter.GROUP group) {
        return group == Carpenter.GROUP.A ? this.freeA : (group == Carpenter.GROUP.B ? this.freeB : this.freeC);
    }

    private void createAndSetCarpenters(int firstAvailableID, Carpenter[] freeCarpenters, Carpenter.GROUP group) {
        for (int i = 0; i < freeCarpenters.length; i++) {
            freeCarpenters[i] = new Carpenter(group, firstAvailableID++);
        }
    }

    private void resetAndFillQueue(Carpenter[] arr, Queue<Carpenter> queue) {
        queue.clear();
        for (int i = 0; i < arr.length; i++) {
            arr[i].reset();
            queue.add(arr[i]);
        }
    }

    /**
     * @return utilization computed for group of carpenters until moment specified by sim. time parameter
     * {@code atMoment}.
     */
    private static double getUtilization(Carpenter[] group, double atMoment) {
        double sum = 0;
        for (Carpenter c : group) {
            sum += c.getSumOfCompletedWorkingTime() / atMoment;
        }
        return sum / group.length;
    }

    /**
     * @param p decimal precision of numbers
     * @param divisor number by which will {@code h} and {@code mean} divided before formatting.
     * @return formatted confidence interval representation
     */
    private static String confIntToStr(double h, double mean, int p, double divisor) {
        h /= divisor;
        mean /= divisor;
        return String.format(("95%% < %."+p+"f | %."+p+"f | %."+p+"f >"), mean-h, mean, mean+h);
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
//        System.out.println(confIntToStr(5, 12, 5, 1));
    }
}
