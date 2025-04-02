package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.utils.DoubleComp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class FurnitureOrder {
    public enum Product {
        TABLE, CHAIR, WARDROBE
    }
    public enum TechStep {
        WOOD_PREPARATION, CARVING, STAINING, ASSEMBLING, FIT_INSTALLATION
    }

    public static class OrderWithPriority {
        private final FurnitureOrder order;
        private final int priority;

        public OrderWithPriority(int priority, FurnitureOrder order) {
            this.order = order;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "OrderWPr{" +
                    " pr=" + priority +
                    "; created=" + order.timeOfCreation +
                    '}';
        }

        public FurnitureOrder getOrder() {
            return order;
        }
    }

    public static class OrderComparator implements Comparator<FurnitureOrder> {
        @Override
        public int compare(FurnitureOrder o1, FurnitureOrder o2) {
            return DoubleComp.compare(o1.timeOfCreation, o2.timeOfCreation);
        }
    }

    public static class WardrobeComparator implements Comparator<OrderWithPriority> {
        @Override
        public int compare(OrderWithPriority o1, OrderWithPriority o2) {
            int cmp = Integer.compare(o1.priority, o2.priority);
            if (cmp != 0)
                return cmp;
            int idx = o1.getOrder().getNextTechStep().ordinal();
            return DoubleComp.compare(o1.getOrder().getLastExecutedTechStepEnd(), o2.getOrder().getLastExecutedTechStepEnd());
        }
    }

    private final double[] techStepsBegin;
    private final double[] techStepsEnd;
    private final Product productType;
    private final double timeOfCreation;
    private final int orderID;
    private int deskID;
    private TechStep nextTechStep;

    /**
     * Technological next step is automatically {@code TechStep.WOOD_PREPARATION}.
     * @param orderID unique identifier order
     * @param timeOfOrderCreation time, when this order came to existence in the system
     */
    public FurnitureOrder(int orderID, double timeOfOrderCreation, Product furnitureType) {
        this.orderID = orderID;
        this.deskID = -1;
        this.timeOfCreation = timeOfOrderCreation;
        this.productType = furnitureType;
        this.techStepsBegin = new double[5];
        this.techStepsEnd = new double[5];
        Arrays.fill(this.techStepsBegin, -1);
        Arrays.fill(this.techStepsEnd, -1);
        this.nextTechStep = TechStep.WOOD_PREPARATION;
    }

    /**
     * @return unique identifier of order
     */
    public int getOrderID() {
        return this.orderID;
    }


    /**
     * Sets desk ID to this order. On this desk ID will be all technological steps executed
     */
    public void setDeskID(int deskID) {
        if (this.deskID > -1)
            throw new RuntimeException("DeskID has already been set (it is set only once)");
        this.deskID = deskID;
    }

    /**
     * @return desk ID on which are all technological steps of this order instance executed
     */
    public int getDeskID() {
        return this.deskID;
    }

    public TechStep getNextTechStep() {
        return this.nextTechStep;
    }

    /**
     * This new {@code techStep} must be consecutive to the one retrieved by {@code getNextTechStep()}.
     */
    public void setNextTechStep(TechStep techStep) {
        if (techStep == TechStep.FIT_INSTALLATION && this.productType != Product.WARDROBE)
            throw new IllegalArgumentException("FIT_INSTALLATION is done within WARDROBE process only");
        if (this.nextTechStep.ordinal()+1 != techStep.ordinal())
            throw new IllegalArgumentException("New techStep is not consecutive with the one that is set right now");
        this.nextTechStep = techStep;
    }

    public void setTechStepBegin(TechStep step, double time) {
        if (this.productType != Product.WARDROBE && step == TechStep.FIT_INSTALLATION)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        this.techStepsBegin[step.ordinal()] = time;
    }

    public void setTechStepEnd(TechStep step, double time) {
        if (this.productType != Product.WARDROBE && step == TechStep.FIT_INSTALLATION)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        this.techStepsEnd[step.ordinal()] = time;
    }

    /**
     * @param step for which step is duration wanted
     * @return time of tech step duration or <code>IllegalArgumentException</code> if this step hasn't been completed
     * yet or this step is not part of its creation process.
     */
    public double getTechStepDuration(TechStep step) {
       if (this.getLastValidIdx() < step.ordinal())
           throw new IllegalArgumentException("This step number is not part of "+this.productType.name()+" process.");
        if (this.techStepsEnd[step.ordinal()] == -1)
            throw new IllegalArgumentException("Step hasn't been completed yet.");
        return this.techStepsEnd[step.ordinal()] - this.techStepsBegin[step.ordinal()];
    }

    public double getLastExecutedTechStepBegin() {
        if (this.nextTechStep == TechStep.WOOD_PREPARATION)
            return this.timeOfCreation;
        return this.techStepsBegin[this.nextTechStep.ordinal()-1];
    }

    public double getLastExecutedTechStepEnd() {
        if (this.techStepsEnd[0] == -1) // no step has been executed yet
            return this.timeOfCreation;
        if (this.techStepsEnd[this.getLastValidIdx()] > -1) // if order is already completed
            return this.techStepsEnd[this.getLastValidIdx()];
        return this.techStepsEnd[this.nextTechStep.ordinal()-1];
    }

    /**
     *
     * @param stepBefore identifies waiting after this step until next technological step was started. If
     *                   <code>null</code>, it identifies waiting time from order's creation until first technological
     *                   step start.
     * @return waiting time or <code>IllegalArgumentException</code> (step not completed or unreasonable param val).
     */
    public double getIntraWaitingDuration(TechStep stepBefore) {
        if (stepBefore == null)
            return this.techStepsBegin[TechStep.WOOD_PREPARATION.ordinal()] - this.timeOfCreation;
        if (this.getLastValidIdx() <= stepBefore.ordinal())
            throw new IllegalArgumentException("Process is already finished or this step is not part of the process");
        if (this.techStepsBegin[stepBefore.ordinal()+1] == -1)
            throw new IllegalArgumentException("Next step hasn't been completed yet.");
        return this.techStepsBegin[stepBefore.ordinal()+1] - this.techStepsEnd[stepBefore.ordinal()];
    }

    /**
     * @return time of whole order processing or <code>RuntimeException</code>, if this order hasn't been
     * completely processed yet.
     */
    public double getOverallProcessingTime() {
        if (this.techStepsEnd[this.getLastValidIdx()] == -1)
            throw new RuntimeException("Order hasn't been completed yet.");
        return this.getTimeOfOrderCompletion() - this.timeOfCreation;
    }

    public Product getProductType() {
        return this.productType;
    }

    public double getTimeOfOrderCreation() {
        return this.timeOfCreation;
    }

    public double getTimeOfOrderCompletion() {
        return this.techStepsEnd[this.getLastValidIdx()];
    }

    @Override
    public String toString() {
        return String.format("Order{type=%s; arisen->completed=%.02f -> %.2f}",
        this.productType, this.timeOfCreation, this.getTimeOfOrderCompletion());
    }

    private int getLastValidIdx() {
        return (this.productType == Product.WARDROBE) ? TechStep.FIT_INSTALLATION.ordinal()
                : TechStep.ASSEMBLING.ordinal();
    }

    public static void main(String[] args) throws InterruptedException {
        FurnitureOrder order = new FurnitureOrder(0,2.0, Product.CHAIR);
        order.setTechStepBegin(TechStep.WOOD_PREPARATION, 4.5);
        order.setTechStepEnd(TechStep.WOOD_PREPARATION, 5.8);
        order.setTechStepBegin(TechStep.CARVING, 17);
        order.setTechStepEnd(TechStep.CARVING, 24);
        order.setTechStepBegin(TechStep.STAINING, 27);
//        order.getOverallProcessingTime();
        System.out.println("Carving dur:"+order.getTechStepDuration(TechStep.CARVING));
//        System.out.println("Staining dur:"+order.getTechStepDuration(TechStep.STAINING));
        order.setTechStepEnd(TechStep.STAINING, 30);
        order.setTechStepBegin(TechStep.ASSEMBLING, 32);
        order.setTechStepEnd(TechStep.ASSEMBLING, 40);
//        order.setTechStepStart(TechStep.FIT_INSTALLATION, 50);
//        order.setTechStepEnd(TechStep.FIT_INSTALLATION, 52);
        System.out.println("Intra dur:"+order.getIntraWaitingDuration(null));
        System.out.println("Complete dur:"+order.getOverallProcessingTime());
        System.out.println("Completion time:"+order.getTimeOfOrderCompletion());
        System.out.println(order);

        System.out.println("\n        QUEUE TEST FOR FURNITURE ORDER");
        PriorityBlockingQueue<FurnitureOrder> orders = new PriorityBlockingQueue<>(10,
                new OrderComparator());
        orders.add(new FurnitureOrder(1,4.0, Product.CHAIR));
        orders.add(new FurnitureOrder(2,1.0, Product.CHAIR));
        orders.add(new FurnitureOrder(3,3.0, Product.CHAIR));
        orders.add(new FurnitureOrder(4,0.0, Product.CHAIR));
        orders.add(new FurnitureOrder(5,2.0, Product.CHAIR));
        System.out.println(orders);
        System.out.println("   * Take:"+orders.take());
        System.out.println(orders);
        System.out.println("   * Take:"+orders.take());
        System.out.println(orders);
        System.out.println("   * Take:"+orders.take());
        System.out.println(orders);
        System.out.println("   * Take:"+orders.take());
        System.out.println(orders);
        System.out.println("   * Take:"+orders.take());
        System.out.println(orders); // ok
        System.out.println("\n        QUEUE TEST FOR FURNITURE ORDER WITH PRIORITY");
        PriorityBlockingQueue<OrderWithPriority> prOrders = new PriorityBlockingQueue<>(10,
                new WardrobeComparator());
        prOrders.add(new OrderWithPriority(1, new FurnitureOrder(6,4.0, Product.CHAIR)));
        prOrders.add(new OrderWithPriority(1, new FurnitureOrder(7,1.0, Product.CHAIR)));
        prOrders.add(new OrderWithPriority(0, new FurnitureOrder(8,3.0, Product.CHAIR)));
        prOrders.add(new OrderWithPriority(1, new FurnitureOrder(9,0.0, Product.CHAIR)));
        prOrders.add(new OrderWithPriority(0, new FurnitureOrder(10,2.0, Product.CHAIR)));
        System.out.println(prOrders);
        System.out.println("   * Take:"+prOrders.take());
        System.out.println(prOrders);
        System.out.println("   * Take:"+prOrders.take());
        System.out.println(prOrders);
        System.out.println("   * Take:"+prOrders.take());
        System.out.println(prOrders);
        System.out.println("   * Take:"+prOrders.take());
        System.out.println(prOrders);
        System.out.println("   * Take:"+prOrders.take());
        System.out.println(prOrders); // ok
    }
}
