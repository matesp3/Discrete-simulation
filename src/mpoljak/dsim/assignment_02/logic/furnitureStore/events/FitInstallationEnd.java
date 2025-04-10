package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
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
        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
        this.sim.returnCarpenter(this.carpenter);
        // * 2. plan order's C end
        this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, null, order));
        // * 3. try to assign order for carpenterC
        Carpenter nextCarpenter = this.tryToAssignOrder(Carpenter.GROUP.C);
        if (nextCarpenter == null)
            return;
        order = nextCarpenter.getCurrentOrder();
        // * 4. plan new C order's processing
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID()) {
                this.sim.receiveWaitingForTechStep(this.getExecutionTime()-order.getWaitingBT(), order.getStep());
                order.setWaitingBT(-1);
                if (nextCarpenter.getCurrentOrder().getStep() == FurnitureOrder.TechStep.FIT_INSTALLATION)
                    this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                else
                    this.sim.addToCalendar(new StainingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
