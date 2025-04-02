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
         * 1. end order executing by carpenter C
         * 2. finish carpenterC job with order
         * 3. start job with carpenterB, if possible
         * --------------------------------------------------------
         * 4. get new C order from queue C, if exists && is some carpenter available
         * 5. plan new C order's processing
         */
        // * 1. end order executing by carpenter C
        this.carpenter.endExecutingStep(this.getExecutionTime());
        // * 2. finish carpenterC job with order
        Carpenter nextCarpenter = this.afterExecutingStep(this.carpenter, FurnitureOrder.TechStep.ASSEMBLING, Carpenter.GROUP.B);
        // * 3. start job with carpenterB, if possible
        if (nextCarpenter != null) {
            if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID()) {
                this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else if (nextCarpenter.getCurrentDeskID() == Carpenter.IN_STORAGE) {
                this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else {
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
        }
        // * 4. get new C order from queue C, if exists
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.C) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.C))
            return;
        FurnitureOrder order = this.sim.getOrderForCarpenter(Carpenter.GROUP.C);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
        // * 5. plan new C order's processing
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
