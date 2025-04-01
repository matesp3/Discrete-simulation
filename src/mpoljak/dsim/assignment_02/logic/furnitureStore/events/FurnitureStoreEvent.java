package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

public abstract class FurnitureStoreEvent extends DiscreteEvent {
    protected final FurnitureStoreSim sim;
    protected final Carpenter carpenter;

    public FurnitureStoreEvent(double executionTime, int secondaryPriority, FurnitureStoreSim simCore,
                               Carpenter carpenter) {
        super(executionTime, secondaryPriority);
        this.sim = simCore;
        this.carpenter = carpenter;
    }

    public FurnitureStoreEvent(double executionTime, FurnitureStoreSim simCore,
                               Carpenter carpenter) {
        super(executionTime);
        this.sim = simCore;
        this.carpenter = carpenter;
    }
}
