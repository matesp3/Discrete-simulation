package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class OrderEnd extends FurnitureProdEvent {
    private FurnitureOrder order;
    public OrderEnd(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter, FurnitureOrder order) {
        super(executionTime, secondaryPriority, simCore, carpenter);
        this.order = order;
    }

    public OrderEnd(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter, FurnitureOrder order) {
        super(executionTime, simCore, carpenter);
        this.order = order;
    }

    @Override
    public void execute() throws InterruptedException {
        this.order.setTimeCompleted(this.getExecutionTime());
        this.sim.releaseDesk(this.order.getDeskID(), this.order);
        this.sim.receiveCompletedOrder(this.order);
    }
}
