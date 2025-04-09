package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class OrderEnd extends FurnitureProdEvent {
    public OrderEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public OrderEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        FurnitureOrder order = this.carpenter.returnOrder(this.getExecutionTime());
        order.setTimeCompleted(this.getExecutionTime());
        this.sim.releaseDesk(order.getDeskID(), order);
        this.sim.returnCarpenter(this.carpenter);
        this.sim.receiveCompletedOrder(order);
    }
}
