package mpoljak.dsim.assignment_02.logic.events.furnitureStore;

import mpoljak.dsim.assignment_02.logic.events.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.sim.FurnitureStore;

public abstract class FurnitureStoreEvent extends DiscreteEvent {
    protected FurnitureStore simCore;

    public FurnitureStoreEvent(double executionTime, int secondaryPriority, FurnitureStore simCore) {
        super(executionTime, secondaryPriority);
        this.simCore = simCore;
    }

    public FurnitureStoreEvent(double executionTime, FurnitureStore simCore) {
        super(executionTime);
        this.simCore = simCore;
    }
}
