package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class StainingEnd extends FurnitureProdEvent {
    public StainingEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public StainingEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. end order executing by carpenterC
         * 2. finish carpenterC job with order
         * 3. start job with carpenterB, if possible
         * --------------------------------------------------------
         * 4. try to assign order for carpenterC
         * 5. plan new order's processing
         */
        // * 1. end order executing by carpenter C
        this.carpenter.endExecutingStep(this.getExecutionTime());
//        ------------------------------v
        // OLD before drying process added (just need to comment this section and uncomment new section, nothing else)
        // * 2. finish carpenterC job with order
        Carpenter nextCarpenter = this.afterExecutingStep(this.carpenter, FurnitureOrder.TechStep.ASSEMBLING, Carpenter.GROUP.B);
        // * 3. start job with carpenterB, if possible
        if (nextCarpenter != null) {
            if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID()) {
                this.sim.receiveWaitingForTechStep(0, FurnitureOrder.TechStep.ASSEMBLING);
                nextCarpenter.getCurrentOrder().setWaitingBT(-1);
                this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else {
                nextCarpenter.getCurrentOrder().setWaitingBT(this.getExecutionTime());
                if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE) {
                    this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
                }
                else { // carpenter is in storage (hasn't been working yet)
                    this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
                }
            }
        }
//        ------------------------------^

//        ------------------------------v
        // NEW after drying process added
//        // *  1. finish carpenterB job with order B
//        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
//        // *  2. release carpenterB
//        this.sim.returnCarpenter(this.carpenter);
//        // *  3. set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
//        order.setNextTechStep(FurnitureOrder.TechStep.DRYING);
//        this.sim.addToCalendar(new DryingStart(this.getExecutionTime(), this.sim, null, order));
//        ------------------------------^

        // * 4. try to assign order for carpenterC
//        NEW(drying) Carpenter nextCarpenter = this.tryToAssignOrder(Carpenter.GROUP.C);
        nextCarpenter = this.tryToAssignOrder(Carpenter.GROUP.C);
        if (nextCarpenter == null)
            return;
        // * 5. plan new C order's processing
        if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID()) {
            this.sim.receiveWaitingForTechStep(this.getExecutionTime()-nextCarpenter.getCurrentOrder().getWaitingBT(), nextCarpenter.getCurrentOrder().getStep());
            nextCarpenter.getCurrentOrder().setWaitingBT(-1);
            if (nextCarpenter.getCurrentOrder().getStep() == FurnitureOrder.TechStep.STAINING)
                this.sim.addToCalendar(new StainingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            else
                this.sim.addToCalendar(new FitInstallationBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        }
        else if (nextCarpenter.getCurrentDeskID() != Carpenter.IN_STORAGE)
            this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
        else
            this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
    }
}
