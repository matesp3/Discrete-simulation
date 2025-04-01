package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class WoodPrepEnd extends FurnitureStoreEvent {
    public WoodPrepEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public WoodPrepEnd(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end execution
         * 2. set next tech step
         * 2.5 carpenter remains
         * 3. plan begin of transfer from storage to hall
         */
        this.carpenter.endExecuting(this.getExecutionTime());
        FurnitureOrder order = this.carpenter.getCurrentOrder();
        order.setDeskID(this.sim.assignFreeDesk(order)); // assign desk for whole process of creating the product from now
        order.setNextTechStep(FurnitureOrder.TechStep.CARVING);
        this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, this.carpenter));
    }
}
