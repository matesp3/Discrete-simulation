package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class WoodPrepBegin extends FurnitureStoreEvent {

    public WoodPrepBegin(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, order, carpenter);
    }

    public WoodPrepBegin(double executionTime, FurnitureStoreSim simCore, FurnitureOrder order, Carpenter carpenter) {
        super(executionTime, simCore, order, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. set order's preparation step begin
         * 2. plan end of prep step execution (generate prep duration)
         */
        this.order.setTechStepBegin(FurnitureOrder.TechStep.WOOD_PREPARATION, this.getExecutionTime());
        this.sim.addToCalendar(new WoodPrepEnd(this.getExecutionTime() + this.sim.nextWoodPreparationDuration(),
                this.sim, this.order, this.carpenter));
    }
}
