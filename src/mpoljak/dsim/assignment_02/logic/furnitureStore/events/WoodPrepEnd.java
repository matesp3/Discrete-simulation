package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class WoodPrepEnd extends FurnitureStoreEvent {
    public WoodPrepEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, order, carpenter);
    }

    public WoodPrepEnd(double executionTime, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, simCore, order, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. set carpenter's work end time
         * 2. set order's prep end && next tech step
         * 3. plan transfer from storage to hall
         */
        this.carpenter.setLastWorkEnd(this.getExecutionTime());
        this.order.setTechStepEnd(FurnitureOrder.TechStep.WOOD_PREPARATION, this.getExecutionTime());
        this.order.setNextTechStep(FurnitureOrder.TechStep.CARVING);
        this.sim.addToCalendar(new TransferBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, this.order,
                this.carpenter));
    }
}
