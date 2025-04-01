package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class TransferBetweenStorageAndHallBegin extends FurnitureStoreEvent {
    public TransferBetweenStorageAndHallBegin(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, order, carpenter);
    }

    public TransferBetweenStorageAndHallBegin(double executionTime, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, simCore, order, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. set carpenter's work begin time
         */
        DiscreteEvent plannedEvent = null;
        double execTime = this.getExecutionTime() + this.sim.nextStorageAndHallTransferDuration();
        switch (this.order.getNextTechStep()) {
            case WOOD_PREPARATION:
                plannedEvent = new WoodPrepBegin(execTime, this.sim, this.order, this.carpenter);
//                this.carpenter.startProcessingNewOrder(this.getExecutionTime());
                break;
                // todo other events planning
            default:
                plannedEvent = null;
        }
        this.sim.addToCalendar(plannedEvent);
    }
}
