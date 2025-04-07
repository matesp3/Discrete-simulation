package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import mpoljak.dsim.utils.DoubleComp;

public class Carpenter {
    public enum GROUP {
        A,B,C;
    }
    public static final int IN_STORAGE = -1;
    private final GROUP group;
    private final int carpenterId;
    private double orderProcessingBT;
    private double orderProcessingET;
    private int deskID;
    private FurnitureOrder currentOrder;

    public Carpenter(GROUP group, int carpenterID) {
        this.group = group;
        this.carpenterId = carpenterID;
        this.deskID = IN_STORAGE;
        this.orderProcessingBT = -1;
        this.orderProcessingET = -1;
        this.currentOrder = null;
    }

    public void reset() {
        this.deskID = IN_STORAGE;
        this.orderProcessingBT = -1;
        this.orderProcessingET = -1;
        this.currentOrder = null;
    }

    /**
     * Carpenter receives {@code newOrder} for which will be executed specific technological step.
     * @param timeOfStart simulation time of assigning {@code newOrder} to this carpenter's instance
     * @throws RuntimeException if Carpenter is already working
     * @throws IllegalArgumentException if order is null
     */
    public void receiveOrder(FurnitureOrder newOrder, double timeOfStart) {
        if (this.isWorking())
            throw new RuntimeException("Carpenter is still working.. Cannot start processing of new order");
        if (newOrder == null)
            throw new IllegalArgumentException("New order for processing not provided (newOrder=null)");
        this.orderProcessingET = -1;
        this.orderProcessingBT = timeOfStart;
        this.currentOrder = newOrder;
    }

    /**
     * Carpenter returns order after executing specific technological step.
     * @param timeOfEnd simulation time of completing work on current order by this carpenter's instance
     */
    public FurnitureOrder returnOrder(double timeOfEnd) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot end processing of order..");
        if (DoubleComp.compare(this.orderProcessingBT, timeOfEnd) == 1) {
            throw new IllegalArgumentException("Order processing beginning > time of end of processing");
        }
        this.orderProcessingET = timeOfEnd;
        FurnitureOrder orderToReturn = this.currentOrder;
        this.currentOrder = null;
        return orderToReturn;
    }

    /**
     * Starts executing of assigned order's next step (which is assumed to be set!) in time specified by parameter
     * {@code timeOfStart}.
     */
    public void startExecutingStep(double timeOfStart) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot start executing tech. step..");
        this.currentOrder.setTechStepBegin(currentOrder.getNextTechStep(), timeOfStart);
    }

    /**
     * Ends executing of assigned order's next step (which is assumed to be set!) in time specified by parameter
     * {@code timeOfEnd}.
     */
    public void endExecutingStep(double timeOfEnd) {
        if (!this.isWorking())
            throw new RuntimeException("Carpenter is not working, so he cannot start executing tech. step..");
        this.currentOrder.setTechStepEnd(currentOrder.getNextTechStep(), timeOfEnd);
    }

    /**
     * @return amount of time of work on lastly processed order
     * @throws RuntimeException if carpenter is working right now or was not working at all
     */
    public double getLastlyProcessedOrderDuration() throws RuntimeException {
        if (this.isWorking() || this.deskID == IN_STORAGE)
            throw new RuntimeException("Carpenter is working (hasn't returned order yet) or is located in the storage");
        return this.orderProcessingET - this.orderProcessingBT;
    }

    /**
     * Sets new position of this carpenter.
     * @param deskID deskID of assigned order or {@code Carpenter.IN_STORAGE} constant if he is located in storage
     */
    public void setCurrentDeskID(int deskID) {
        this.deskID = deskID;
    }

    /**
     * @return group ID, in which is carpenter working.
     */
    public GROUP getGroup() {
        return this.group;
    }

    /**
     * @return unique identifier of carpenter
     */
    public int getCarpenterId() {
        return this.carpenterId;
    }

    /**
     * @return ID of desk where carpenter is standing right now or {@code Carpenter.IN_STORAGE} value if he is in wood
     * storage.
     */
    public int getCurrentDeskID() {
        return this.deskID;
    }

    /**
     * @return order that is currently being processed by this carpenter's instance
     */
    public FurnitureOrder getCurrentOrder() {
        return this.currentOrder;
    }

    /**
     * @return time of beginning of lastly processing order
     */
    public double getOrderProcessingBT() {
        return this.orderProcessingBT;
    }

    /**
      * @return time of end of lastly processed order
     */
    public double getOrderProcessingET() {
        return this.orderProcessingET;
    }

    /**
     * @return <code>true</code> if he is processing (if he owns) some instance of order
     */
    public boolean isWorking() {
        return this.currentOrder != null;
    }

    @Override
    public String toString() {
        return String.format("Carp{%s;carpID=%d;desk=%d;orderID=%d}", this.group, this.carpenterId, this.deskID,
                this.isWorking() ? this.currentOrder.getOrderID() : null);
    }

    public static void main(String[] args) {
        Carpenter carpenter = new Carpenter(GROUP.A, 1);
        FurnitureOrder order = new FurnitureOrder(14, 0.25, FurnitureOrder.Product.CHAIR);
        order.setDeskID(1);
        System.out.println(carpenter.getGroup());
        System.out.println(carpenter.isWorking());
        System.out.println(carpenter.getOrderProcessingBT());
        carpenter.receiveOrder(order,5.0);
        System.out.println(carpenter.getOrderProcessingBT());
        System.out.println(carpenter.isWorking());
//        System.out.println(carpenter.getLastlyProcessedOrderDuration()); // ok
        carpenter.returnOrder(56.4);
        System.out.println(carpenter.getOrderProcessingET());
        System.out.println(carpenter.isWorking());
        System.out.println(carpenter.getLastlyProcessedOrderDuration());
        carpenter.receiveOrder(order,60.0);
        System.out.println(carpenter.getOrderProcessingBT());
        System.out.println(carpenter.isWorking());
//        System.out.println(carpenter.getOrderProcessingET());
//        System.out.println(carpenter.getLastlyProcessedOrderDuration()); // ok

//        System.out.println("compare(false, true): "+Boolean.compare(false, true));
//        System.out.println("compare(true, true): "+Boolean.compare(true, true));
//        System.out.println("compare(true, false): "+Boolean.compare(true, false));
//        System.out.println("compare(false, false): "+Boolean.compare(false, false));
    }
}
