package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class OrderArrival extends FurnitureStoreEvent {
    public OrderArrival(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public OrderArrival(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
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
        FurnitureOrder newOrder = new FurnitureOrder(this.sim.assignOrderID(), this.sim.getSimTime(),
                this.sim.nextProductType());
        // * 2. plan order's processing or enqueuing for waiting
        Carpenter carpenterA = this.sim.getFirstFreeCarpenter(Carpenter.GROUP.A);
        if (carpenterA != null) {
            if (carpenterA.getDeskID() == Carpenter.IN_STORAGE)
                this.sim.addToCalendar(new WoodPrepBeginning(this.getExecutionTime(), this.sim, carpenterA));
            else
                this.sim.addToCalendar(new TransferBetweenStorageAndHallBegin(this.getExecutionTime(), this.sim, carpenterA));

            carpenterA.receiveOrder(newOrder, this.getExecutionTime(), this.sim.assignFreeDesk(carpenterA));
        }
        else {
            this.sim.enqueueForNextProcessing(newOrder);
        }
        // * 3. plan time of new order arrival
        this.setExecutionTime(this.getExecutionTime() + this.sim.nextUntilOrderArrivalDuration());
        this.sim.addToCalendar(this);
    }
}
