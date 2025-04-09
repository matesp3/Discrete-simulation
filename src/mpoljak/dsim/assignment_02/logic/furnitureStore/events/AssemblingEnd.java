package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class AssemblingEnd extends FurnitureProdEvent {
    public AssemblingEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public AssemblingEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order executing by carpenter B
         * 2. determine if product is completed
         *  2.A1 finish carpenterB job with order B and plan next job
         *  2.B1 plan end of completed order
         * --------------------------------------------------------
         * 3. try to assign order for carpenterB
         * 4. plan new B order's processing
         */
        Carpenter nextCarpenter;
        // * 1. end order B (may lead C) executing
        this.carpenter.endExecutingStep(this.getExecutionTime());
        // * 2. determine if product is completed
        if (this.carpenter.getCurrentOrder().getProductType() == FurnitureOrder.Product.WARDROBE) {
            // * 2.A1 finish carpenterB job with order B and plan next job
            nextCarpenter = this.afterExecutingStep(this.carpenter, FurnitureOrder.TechStep.FIT_INSTALLATION, Carpenter.GROUP.C);
            if (nextCarpenter != null) {
                if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID())
                    this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                else if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE)
                    this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                else
                    this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
            }
        }
        else {//  *  2.B1 plan end of completed order
            FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
            this.sim.returnCarpenter(this.carpenter);
            this.sim.addToCalendar(new OrderEnd(this.getExecutionTime(), this.sim, null, order));
        }
        // * 3. try to assign order for carpenterB
        nextCarpenter = this.tryToAssignOrder(Carpenter.GROUP.B);
        if (nextCarpenter == null)
            return;
        // * 4. plan new B order's processing
        if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID())
            this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        else if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE)
            this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
