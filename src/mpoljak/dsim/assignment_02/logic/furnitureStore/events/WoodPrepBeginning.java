package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class WoodPrepBeginning extends FurnitureStoreEvent {

    public WoodPrepBeginning(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore,  carpenter);
    }

    public WoodPrepBeginning(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. start of wood preparation execution
         * 2. plan end of the prep step execution (generate prep duration)
         */
        this.carpenter.startExecutingStep(this.getExecutionTime());
        this.sim.addToCalendar(new WoodPrepEnd(this.getExecutionTime() + this.sim.nextWoodPreparationDuration(),
                this.sim, this.carpenter));
    }
}
