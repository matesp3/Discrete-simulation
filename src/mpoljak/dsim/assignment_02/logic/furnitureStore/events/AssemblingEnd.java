package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class AssemblingEnd extends FurnitureStoreEvent {
    public AssemblingEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public AssemblingEnd(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order B (may lead C) executing
         * 2. determine if product is completed
         *  2.A1 finish carpenterB job with order B
         *  2.A2 release carpenterB
         *  2.A3 set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
         *  2.B1 plan end of completed order
         * --------------------------------------------------------
         * 3. get new B order from queue B, if exists && is some carpenter available
         * 4. plan new B order's processing
         */
        // * 1. end order B (may lead C) executing
        this.carpenter.endExecuting(this.getExecutionTime());
        Carpenter nextCarpenter;
        FurnitureOrder order;
        // * 2. determine if product is completed
        if (this.carpenter.getCurrentOrder().getProductType() == FurnitureOrder.Product.WARDROBE) {
            // *  2.A1 finish carpenterB job with order B
            order = this.carpenter.returnOrder(this.getExecutionTime());
            // *  2.A2 release carpenterB
            this.sim.returnCarpenter(this.carpenter);
            // *  2.A3 set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
            order.setNextTechStep(FurnitureOrder.TechStep.FIT_INSTALLATION);
            nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
            if (nextCarpenter != null) {
                nextCarpenter.receiveOrder(order, this.getExecutionTime());
                if (nextCarpenter.getCurrentDeskID() == order.getDeskID()) {
                    this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                }
                else if (nextCarpenter.getCurrentDeskID() == Carpenter.IN_STORAGE) {
                    this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
                }
                else {
                    this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                }
            }
            else {
                this.sim.enqueueForNextProcessing(order);
            }
        }
        else {
            //  *  2.B1 plan end of completed order
            this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, this.carpenter));
        }
        // * 3. get new B order from queue B, if exists && is some carpenter available
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.B) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.B))
            return;
        order = this.sim.getOrderForCarpenter(Carpenter.GROUP.B);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.B);
        // * 4. plan new B order's processing
        nextCarpenter.receiveOrder(order, this.getExecutionTime());
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID())
                this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            else
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
