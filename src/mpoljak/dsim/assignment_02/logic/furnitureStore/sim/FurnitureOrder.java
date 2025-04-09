package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.utils.DoubleComp;
import mpoljak.dsim.utils.Formatter;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class FurnitureOrder {
    public enum Product {
        TABLE, CHAIR, WARDROBE
    }
    public enum TechStep {
        WOOD_PREPARATION, CARVING, STAINING, ASSEMBLING, FIT_INSTALLATION //, DRYING - if added, need to be fit where it belongs chronologically
    }

    private final int orderID;
    private final double timeCreated;
    private final Product productType;

    private int deskID;
    private double stepBT;
    private double stepET;
    private double waitingBT;
    private double timeCompleted;
    private TechStep step;

    /**
     * Technological next step is automatically {@code TechStep.WOOD_PREPARATION}.
     * @param orderID unique identifier order
     * @param timeOfOrderCreation time, when this order came to existence in the system
     */
    public FurnitureOrder(int orderID, double timeOfOrderCreation, Product furnitureType) {
        this.orderID = orderID;
        this.timeCreated = timeOfOrderCreation;
        this.productType = furnitureType;

        this.deskID = -1;
        this.stepBT = -1;
        this.stepET = -1;
        this.waitingBT = -1;
        this.timeCompleted = -1;
        this.step = TechStep.WOOD_PREPARATION;
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

    public TechStep getStep() {
        return this.step;
    }

    /**
     * This new {@code techStep} must be consecutive to the one retrieved by {@code getNextTechStep()}.
     */
    public void setStep(TechStep techStep) {
        this.stepCheck(techStep);
        this.step = techStep;
    }

    public void setStepBT(double time) {
        this.stepBT = time;
    }

    public void setStepET(double time) {
        this.stepET = time;
    }

    /**
     * @return time of whole order processing or <code>RuntimeException</code>, if this order hasn't been
     * completely processed yet.
     */
    public double getOverallTime() {
        if (timeCompleted < 0)
            throw new RuntimeException("Order hasn't been completed yet.");
        return this.timeCompleted - this.timeCreated;
    }

    public Product getProductType() {
        return this.productType;
    }

    /**
     * @return time when was order created
     */
    public double getTimeCreated() {
        return this.timeCreated;
    }

    /**
     * @param timeCompleted when was order fully completed
     */
    public void setTimeCompleted(double timeCompleted) {
        this.timeCompleted = timeCompleted;
    }

    /**
     * @return timeCompleted when was order fully completed
     */
    public double getTimeCompleted() {
        return this.timeCompleted;
    }

    /**
     * @return time of waiting beginning for next technological step
     */
    public double getWaitingBT() {
        return this.waitingBT;
    }

    /**
     * @param waitingBT time of waiting beginning for next technological step
     */
    public void setWaitingBT(double waitingBT) {
        this.waitingBT = waitingBT;
    }

    @Override
    public String toString() {
        return String.format("Order: [orderID=%5d ;desk=%5d; waitingBT=%s; stepBT=%s; setET=%s; next=%-16s ;%-8s; [created=%.02f => completed=%.02f] ]",
        this.orderID, this.deskID, Formatter.getStrDateTime(this.waitingBT, 8, 6),
                Formatter.getStrDateTime(this.stepBT, 8, 6),
                Formatter.getStrDateTime(this.stepET, 8, 6),
                this.step, this.productType, this.timeCreated, this.timeCompleted);
    }

    private void stepCheck(TechStep newStep) {
        if (newStep == TechStep.FIT_INSTALLATION && this.productType != Product.WARDROBE)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        if (newStep.ordinal() != this.step.ordinal()+1) // causality check
            throw new IllegalArgumentException("Steps causality of order violated [currentStep="+this.step +";newStep="+newStep+"]");
    }

    public static void main(String[] args) throws InterruptedException {
        FurnitureOrder order = new FurnitureOrder(0,2.0, Product.CHAIR);
        order.setStepBT(4.5);
        order.setStepET(5.8);
        order.setStepBT(17);
        order.setStepET(24);
        order.setStepBT(27);
        order.setStepET(30);
        order.setStepBT( 32);
        order.setStepET(40);
        order.setTimeCompleted(40);
        System.out.println("Complete dur:"+order.getOverallTime());
        System.out.println("Completion time:"+order.getTimeCompleted());
        System.out.println(order);

        System.out.println("\n        QUEUE TEST FOR FURNITURE ORDER with time of creation as a priority");
        PriorityBlockingQueue<FurnitureOrder> orders = new PriorityBlockingQueue<>(10, new Comparator<FurnitureOrder>() {
            @Override
            public int compare(FurnitureOrder o1, FurnitureOrder o2) {
                return DoubleComp.compare(o1.timeCreated, o2.timeCreated);
            }
        });
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
    }
}
