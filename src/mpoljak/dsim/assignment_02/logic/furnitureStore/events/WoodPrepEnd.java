package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class WoodPrepEnd extends FurnitureProdEvent {
    public WoodPrepEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public WoodPrepEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end execution
         * 2. set next tech step
         * 2.1 note: carpenter remains
         * 3. plan begin of transfer from storage to hall
         */
        this.carpenter.endExecutingStep(this.getExecutionTime());
        this.carpenter.getCurrentOrder().setStep(FurnitureOrder.TechStep.CARVING);
        this.carpenter.getCurrentOrder().setWaitingBT(-1); // <-- CARVING is NOT part of waiting, but part of work
        this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, this.carpenter));
    }
}
