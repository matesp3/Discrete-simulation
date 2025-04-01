package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class MovingBetweenStorageAndHallBegin extends FurnitureStoreEvent {
    public MovingBetweenStorageAndHallBegin(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public MovingBetweenStorageAndHallBegin(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. generate time needed for moving
         * 2. determine event to be planned
         * 3. plan the event
         */
        // * 1. generate time needed for transferring
        double startExecTime = this.getExecutionTime() + this.sim.nextStorageAndHallMovingDuration();
        // * 2. determine event to be planned
        FurnitureStoreEvent plannedEvent;
        switch (this.carpenter.getCurrentOrder().getNextTechStep()) {
            case WOOD_PREPARATION:
                plannedEvent = new WoodPrepBeginning(startExecTime, this.sim, this.carpenter);
                break;
            case CARVING:
                plannedEvent = new CarvingBeginning(startExecTime, this.sim, this.carpenter);
                break;
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
        this.carpenter.setCurrentDeskID(Carpenter.IN_STORAGE); // now he is in storage
        // * 3. plan the event
        this.sim.addToCalendar(plannedEvent);
    }
}
