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
         * 1. set carpenter's work begin time
         * 2. set order's prep begin
         * 3. plan end of execution (generate prep duration)
         */
        this.carpenter.setLastWorkStart(this.getExecutionTime());
        this.order.setTechStepStart(FurnitureOrder.TechStep.WOOD_PREPARATION, this.getExecutionTime());
        this.sim.addToCalendar(new WoodPrepEnd(this.getExecutionTime() + this.sim.nextWoodPreparationDuration(),
                this.sim, this.order, this.carpenter));
    }
}
