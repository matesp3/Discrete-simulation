package mpoljak.dsim.assignment_02.logic.ticketSelling.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSellingSim;

public abstract class TicketSellingEvent extends DiscreteEvent {
    protected TicketSellingSim simCore;

    public TicketSellingEvent(double executionTime, int secondaryPriority, TicketSellingSim simCore) {
        super(executionTime, secondaryPriority);
        this.simCore = simCore;
    }

    public TicketSellingEvent(double executionTime, TicketSellingSim simCore) {
        super(executionTime);
        this.simCore = simCore;
    }
}
