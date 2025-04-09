package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdEventResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.OrderResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class OrderArrival extends FurnitureProdEvent {
    public OrderArrival(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public OrderArrival(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. create order
         * 2. plan order's processing or enqueuing for waiting
         * 3. plan time of new order arrival
         */
        // * 1. create order
        FurnitureOrder newOrder = new FurnitureOrder(this.sim.assignOrderID(), this.getExecutionTime(),
                this.sim.nextProductType());
        newOrder.setDeskID(this.sim.assignFreeDesk(newOrder)); // assign desk for whole process of creating the product from now
        // * 2. plan order's processing or enqueuing for waiting
        Carpenter carpenterA = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.A);
        if (carpenterA != null) {
            carpenterA.receiveOrder(newOrder, this.getExecutionTime());
            if (carpenterA.getCurrentDeskID() != Carpenter.IN_STORAGE)
                this.sim.addToCalendar(new MovingBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, carpenterA));
            else
                this.sim.addToCalendar(new WoodPrepBeginning(this.getExecutionTime(), this.sim, carpenterA));
        }
        else {

            this.sim.enqueueForNextProcessing(newOrder);
        }
        // * 3. plan time of new order arrival
        this.setExecutionTime(this.getExecutionTime() + this.sim.nextUntilOrderArrivalDuration());
        this.sim.addToCalendar(this);
    }
}
