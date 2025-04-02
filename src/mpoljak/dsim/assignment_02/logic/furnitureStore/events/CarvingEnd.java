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
         * 1. end order executing by carpenter A
         * 2. finish carpenterA job with order
         * 3. start job with carpenterC, if possible
         * --------------------------------------------------------
         * 4. get new A order from queue A, if exists && is some carpenter available
         * 5. plan new A order's processing
         */
        // * 1. end order executing by carpenter A
        this.carpenter.endExecutingStep(this.getExecutionTime());
        // * 2. finish carpenterA job with order
        Carpenter nextCarpenter = this.afterExecutingStep(this.carpenter, FurnitureOrder.TechStep.STAINING, Carpenter.GROUP.C);
        // * 3. start job with carpenterC, if possible
        if (nextCarpenter != null) {
            if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID()) {
                this.sim.addToCalendar(new StainingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else if (nextCarpenter.getCurrentDeskID() == Carpenter.IN_STORAGE) {
                this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else {
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
        }
        // * 4. get new A order from queue A, if exists
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.A) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.A))
            return;
        FurnitureOrder order = this.sim.getOrderForCarpenter(Carpenter.GROUP.A);
        nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.A);
        // * 5. plan new A order's processing
        nextCarpenter.receiveOrder(order, this.getExecutionTime());
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE)
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
        else
            this.sim.addToCalendar(new WoodPrepBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
