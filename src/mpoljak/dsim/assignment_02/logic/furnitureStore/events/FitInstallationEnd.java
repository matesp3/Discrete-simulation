package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class FitInstallationEnd extends FurnitureStoreEvent {
    public FitInstallationEnd(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public FitInstallationEnd(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order C executing
         * 2. plan order's C end
         * --------------------------------------------------------
         * 3. get new C order from queue C, if exists && is some carpenter available
         * 4. plan new C order's processing
         */
        // * 1. end order C executing
        this.carpenter.endExecutingStep(this.getExecutionTime());
        // * 2. plan order's C end
        this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, this.carpenter));
        // * 3. get new C order from queue C, if exists && is some carpenter available
        if (this.sim.hasNotWaitingOrder(Carpenter.GROUP.C) || this.sim.hasNotAvailableCarpenter(Carpenter.GROUP.C))
            return;
        FurnitureOrder order = this.sim.getOrderForCarpenter(Carpenter.GROUP.C);
        Carpenter nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.C);
        // * 4. plan new C order's processing
        nextCarpenter.receiveOrder(order, this.getExecutionTime());
        if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
            if (nextCarpenter.getCurrentDeskID() == order.getDeskID())
                this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            else
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
