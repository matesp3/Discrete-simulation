package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class MovingAmongDesksBeginning extends FurnitureStoreEvent {
    public MovingAmongDesksBeginning(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public MovingAmongDesksBeginning(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. generate time needed for moving among desks
         * 2. determine event to be planned
         * 3. plan the event
         */
        // * 1. generate time needed for transferring
        double startExecTime = this.getExecutionTime() + this.sim.nextDeskMovingDuration();
        // * 2. determine event to be planned
        FurnitureStoreEvent plannedEvent;
        switch (this.carpenter.getCurrentOrder().getNextTechStep()) {
            case STAINING:
                plannedEvent = new StainingBeginning(startExecTime, this.sim, this.carpenter);
                break;
            case ASSEMBLING:
                plannedEvent = new AssemblingBeginning(startExecTime, this.sim, this.carpenter);
                break;
            case FIT_INSTALLATION: // could be in the beginning, when only one wardrobe is being created and some carpenter is still in the storage
                plannedEvent = new FitInstallationBeginning(startExecTime, this.sim, this.carpenter);
                break;
            default:
                plannedEvent = null;
        }
        // * 3. plan the event
        this.sim.addToCalendar(plannedEvent);
    }
}
