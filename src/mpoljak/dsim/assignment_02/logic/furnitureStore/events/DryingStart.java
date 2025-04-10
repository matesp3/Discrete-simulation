package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class DryingStart extends FurnitureProdEvent {
    private FurnitureOrder order;
    public DryingStart(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter,
                       FurnitureOrder order) {
        super(executionTime, secondaryPriority, simCore, carpenter);
        this.order = order;
    }

    public DryingStart(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter, FurnitureOrder order) {
        super(executionTime, simCore, carpenter);
        this.order = order;
    }

    @Override
    public void execute() throws InterruptedException {
        this.sim.addToCalendar(
                new DryingEnd(this.getExecutionTime()+this.sim.nextDryingDuration(), this.sim, this.carpenter, this.order)
        );
    }
}
