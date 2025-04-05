package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class FitInstallationEnd extends FurnitureProdEvent {
    public FitInstallationEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public FitInstallationEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order C executing
         * 2. plan order's C end
         * --------------------------------------------------------
         * 3. try to assign order for carpenterC
         * 4. plan new C order's processing
         */
        // * 1. end order C executing
        this.carpenter.endExecutingStep(this.getExecutionTime());
        // * 2. plan order's C end
        this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, this.carpenter));
        // * 3. try to assign order for carpenterC
        Carpenter nextCarpenter = this.tryToAssignOrder(Carpenter.GROUP.C);
        if (nextCarpenter == null)
            return;
        // * 4. plan new C order's processing
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
            if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID())
                this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            else
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
