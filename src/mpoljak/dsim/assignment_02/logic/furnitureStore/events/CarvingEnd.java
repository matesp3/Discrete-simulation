package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class CarvingEnd extends FurnitureStoreEvent {
    public CarvingEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, order, carpenter);
    }

    public CarvingEnd(double executionTime, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, simCore, order, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. set carpenter's work end time
         */
        this.carpenter.returnOrder(this.getExecutionTime());
    }
}
