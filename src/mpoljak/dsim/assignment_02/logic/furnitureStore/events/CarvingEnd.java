package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class CarvingEnd extends FurnitureStoreEvent {
    public CarvingEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public CarvingEnd(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order A (going to be C) executing
         * 2. finish carpenterA job with order A(C)
         * 3. release carpenterA
         * 4. set next tech step to order A(C)
         * 5. plan order's A(C) processing or enqueuing for waiting
         * --------------------------------------------------------
         * 6. get new A order from queue A, if exists && is some carpenter available
         * 7. plan new A order's processing
         */
        // * 1. end order A (going to be C) executing
        this.carpenter.endExecuting(this.getExecutionTime());
        // * 2. finish carpenterA job with order A(C)
        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
        // * 3. release carpenterA
        this.sim.returnCarpenter(this.carpenter);
        // * 4. set next tech step to order A(C)
        order.setNextTechStep(FurnitureOrder.TechStep.STAINING);
        // * 5. plan A(C) order's processing or enqueuing for waiting
        Carpenter nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
        if (nextCarpenter != null) {
            nextCarpenter.receiveOrder(order, this.getExecutionTime());
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID()) {
                this.sim.addToCalendar(new StainingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
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
        // * 6. get new A order from queue A, if exists
        if (!this.sim.hasWaitingOrder(Carpenter.GROUP.A) || !this.sim.hasAvailableCarpenter(Carpenter.GROUP.A))
            return;
        order = this.sim.getOrderForCarpenter(Carpenter.GROUP.A);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.A);
        // * 7. plan new A order's processing
        nextCarpenter.receiveOrder(order, this.getExecutionTime());
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE)
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
        else
            this.sim.addToCalendar(new WoodPrepBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
