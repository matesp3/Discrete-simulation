package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class StainingEnd extends FurnitureStoreEvent {
    public StainingEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public StainingEnd(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order C (going to be B) executing
         * 2. finish carpenterC job with order C(B)
         * 3. release carpenterC
         * 4. set next tech step to order C(B)
         * 5. plan order's C(B) processing or enqueuing for waiting
         * --------------------------------------------------------
         * 6. get new C order from queue C, if exists && is some carpenter available
         * 7. plan new C order's processing
         */
        // * 1. end order C (going to be B) executing
        this.carpenter.endExecuting(this.getExecutionTime());
        // * 2. finish carpenterC job with order C(B)
        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
        // * 3. release carpenterC
        this.sim.returnCarpenter(this.carpenter);
        // * 4. set next tech step to order C(B)
        order.setNextTechStep(FurnitureOrder.TechStep.ASSEMBLING);
        // * 5. plan C(B) order's processing or enqueuing for waiting
        Carpenter nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.B);
        if (nextCarpenter != null) {
            nextCarpenter.receiveOrder(order, this.getExecutionTime());
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID()) {
                this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
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
        // * 6. get new C order from queue C, if exists
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.C) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.C))
            return;
        order = this.sim.getOrderForCarpenter(Carpenter.GROUP.C);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
        // * 7. plan new C order's processing
        nextCarpenter.receiveOrder(order, this.getExecutionTime());
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID())
                this.sim.addToCalendar(new StainingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            else
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
