package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public class FitInstallationBeginning extends FurnitureStoreEvent {
    public FitInstallationBeginning(double executionTime, int secondaryPriority, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, secondaryPriority, simCore, carpenter);
    }

    public FitInstallationBeginning(double executionTime, FurnitureStoreSim simCore, Carpenter carpenter) {
        super(executionTime, simCore, carpenter);
    }

    @Override
    public void execute() throws InterruptedException {
        this.beforePlanOfBeginning();
        this.sim.addToCalendar(
                new FitInstallationEnd(this.getExecutionTime()+this.sim.nextFitInstallationDuration(),
                        this.sim, this.carpenter)
        );
    }
}
