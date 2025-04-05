package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class DryingEnd extends FurnitureProdEvent {
    private FurnitureOrder order;
    public DryingEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter,
                     FurnitureOrder order) {
        super(executionTime, secondaryPriority, simCore, carpenter);
        this.order = order;
    }

    public DryingEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter, FurnitureOrder order) {
        super(executionTime, simCore, carpenter);
        this.order = order;
    }

    @Override
    public void execute() throws InterruptedException {
//        FurnitureOrder order = this.carpenter.getCurrentOrder();
        this.order.setNextTechStep(FurnitureOrder.TechStep.ASSEMBLING);
        Carpenter nextCarpenter = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.B);
        if (nextCarpenter == null)
            this.sim.enqueueForNextProcessing(this.order);
        else
            nextCarpenter.receiveOrder(this.order, this.getExecutionTime());
        // * 3. start job with carpenterB, if possible
        if (nextCarpenter != null) {
            if (nextCarpenter.getCurrentDeskID() == nextCarpenter.getCurrentOrder().getDeskID()) {
                this.sim.addToCalendar(new AssemblingBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else if (nextCarpenter.getCurrentDeskID() == Carpenter.IN_STORAGE) {
                this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, nextCarpenter));
            }
            else {
                this.sim.addToCalendar(new MovingAmongDesksBeginning(this.getExecutionTime(), this.sim, nextCarpenter));
            }
        }
    }
}
