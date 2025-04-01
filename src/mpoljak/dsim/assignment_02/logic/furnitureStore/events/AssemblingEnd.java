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
         * 2. finish carpenterB job with order B(may lead C)
         * 3. release carpenterB
         * 4. set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
         * --------------------------------------------------------
         * 5. get new B order from queue B, if exists && is some carpenter available
         * 6. plan new B order's processing
         */
        // * 1. end order B (may lead C) executing
        this.carpenter.endExecuting(this.getExecutionTime());
        // * 2. finish carpenterB job with order B(may lead C)
        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
        // * 3. release carpenterB
        this.sim.returnCarpenter(this.carpenter);
        // * 4. set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
        Carpenter nextCarpenter;
        if (order.getProductType() == FurnitureOrder.Product.WARDROBE) {
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
            this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, this.carpenter));
        }
        // * 6. get new B order from queue B, if exists && is some carpenter available
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.B) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.B))
            return;
        order = this.sim.getOrderForCarpenter(Carpenter.GROUP.B);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.B);
        // * 7. plan new B order's processing
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
