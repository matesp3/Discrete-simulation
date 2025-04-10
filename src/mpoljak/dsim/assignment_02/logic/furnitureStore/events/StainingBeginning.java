package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public class StainingBeginning extends FurnitureProdEvent {
    public StainingBeginning(double executionTime, int secondaryPriority, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public StainingBeginning(double executionTime, FurnitureProductionSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        this.beforePlanOfBeginning();
        this.sim.addToCalendar(
                new StainingEnd(
                        this.getExecutionTime()+this.sim.nextStainingDuration(
                                this.carpenter.getCurrentOrder().getProductType()), this.sim, this.carpenter)
        );
    }
}
