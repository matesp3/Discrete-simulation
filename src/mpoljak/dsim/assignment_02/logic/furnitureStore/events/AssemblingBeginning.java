package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class AssemblingBeginning extends FurnitureStoreEvent {

    public AssemblingBeginning(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public AssemblingBeginning(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        /*
         * 1. update carpenter's desk position regarding order's deskID
         * 2. start tech step executing
         * 3. plan end of tech step executing (generate carving duration)
         */
        // * 1. update carpenter's desk position regarding order's deskID
        this.carpenter.setCurrentDeskID(this.carpenter.getCurrentOrder().getDeskID());
        // * 2. start tech step executing
        this.carpenter.startExecuting(this.getExecutionTime());
        // * 3. plan end of tech step executing
        this.sim.addToCalendar(
                new AssemblingEnd(
                        this.getExecutionTime()+this.sim.nextAssemblingDuration(
                                this.carpenter.getCurrentOrder().getProductType()), this.sim, this.carpenter)
        );
    }
}
