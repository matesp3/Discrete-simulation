package mpoljak.dsim.assignment_02.logic.furnitureStore.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

public abstract class FurnitureProdEvent extends DiscreteEvent {
    private static final int OID = 8;
    protected final FurnitureProductionSim sim;
    protected final Carpenter carpenter;

    public FurnitureProdEvent(double executionTime, int secondaryPriority, FurnitureProductionSim simCore,
                              Carpenter carpenter) {
        super(executionTime, secondaryPriority);
        this.sim = simCore;
        this.carpenter = carpenter;
    }

    public FurnitureProdEvent(double executionTime, FurnitureProductionSim simCore,
                              Carpenter carpenter) {
        super(executionTime);
        this.sim = simCore;
        this.carpenter = carpenter;
    }

    protected final void beforePlanOfBeginning() {
        /*
         * 1. update carpenter's desk position regarding order's deskID
         * 2. start tech step executing
         * 3. plan end of tech step executing (generate carving duration)
         */
        // * 1. update carpenter's desk position regarding order's deskID
        this.carpenter.setCurrentDeskID(this.carpenter.getCurrentOrder().getDeskID());
        // * 2. start tech step executing
        this.carpenter.startExecutingStep(this.getExecutionTime());
        // * 3. plan end of tech step executing <-- has to be done in child
    }

    /**
     * Receives completed order's step from {@code currentCarpenter} and assigns it to next carpenter or enqueues order
     * to wait for next processing. It also releases {@code currentCarpenter} from work and returns to simulation.
     * @param currentCarpenter carpenter that ended execution of order's step
     * @param nextStep this step will be set as next step of {@code currentCarpenter}
     * @param nextCarpenterGroup group of next carpenter
     * @return next carpenter of {@code nextCarpenterGroup} with received order from {@code currentCarpenter}
     * or {@code null} if order has been enqueued to wait for
     * processing due to no free next carpenter.
     */
    protected final Carpenter afterExecutingStep(Carpenter currentCarpenter, FurnitureOrder.TechStep nextStep,
                                                 Carpenter.GROUP nextCarpenterGroup) {
        // *  1. finish carpenterB job with order B
        FurnitureOrder order = currentCarpenter.returnOrder(this.getExecutionTime());
        // *  2. release carpenterB
        this.sim.returnCarpenter(currentCarpenter);
        // *  3. set next tech step to order B(may lead C or ending approached) & plan order's B(may lead C) processing
        order.setStep(nextStep);
        currentCarpenter = this.sim.getFirstFreeCarpenter(nextCarpenterGroup);
        if (currentCarpenter == null)
            this.sim.enqueueForNextProcessing(order);
        else
            currentCarpenter.receiveOrder(order, this.getExecutionTime());
        return currentCarpenter;
    }

    /**
     * Tries to assign some order from queue of waiting order of type {@code group}. If it finds some waiting order
     * it assigns it to carpenter also.
     * @param group of carpenter that is wanted to get some order from queue
     * @return carpenter with assigned order or {@code null} if free carpenter or waiting order is missing
     */
    protected final Carpenter tryToAssignOrder(Carpenter.GROUP group) {
        // * 1. get new order from queue, if exists && is some carpenter available
        if (this.sim.hasNotWaitingOrder(group) || this.sim.hasNotAvailableCarpenter(group))
            return null;
        Carpenter newCarpenter = this.sim.getFirstFreeCarpenter(group);
        FurnitureOrder order = this.sim.getOrderForCarpenter(group);
        // * 2. plan new order's processing
        newCarpenter.receiveOrder(order, this.getExecutionTime());
        return newCarpenter;
    }
}
