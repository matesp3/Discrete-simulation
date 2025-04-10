package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;
import mpoljak.dsim.utils.DoubleComp;

public class MovingAmongDesksBeginning extends FurnitureProdEvent {
    public MovingAmongDesksBeginning(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public MovingAmongDesksBeginning(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
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
        double movingDuration = this.sim.nextDeskMovingDuration();
        double startExecTime = this.getExecutionTime() + movingDuration;
        // * 2. determine event to be planned
        FurnitureOrder order = this.carpenter.getCurrentOrder();
        FurnitureProdEvent plannedEvent;
        switch (order.getStep()) {
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
        if (DoubleComp.compare(order.getWaitingBT(), 0) > -1) { // waitingBT >= 0 --> active waiting for start of work
            this.sim.receiveWaitingForTechStep(this.getExecutionTime() - order.getWaitingBT() + movingDuration, order.getStep());
            order.setWaitingBT(-1);
        }
        // * 3. plan the event
        this.sim.addToCalendar(plannedEvent);
    }
}
