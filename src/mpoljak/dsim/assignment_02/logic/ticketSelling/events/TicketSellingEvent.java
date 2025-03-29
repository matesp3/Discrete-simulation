package mpoljak.dsim.assignment_02.logic.ticketSelling.events;

import mpoljak.dsim.assignment_02.logic.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSelling;

public abstract class TicketSellingEvent extends DiscreteEvent {
    protected TicketSelling simCore;

    public TicketSellingEvent(double executionTime, int secondaryPriority, TicketSelling simCore) {
        super(executionTime, secondaryPriority);
        this.simCore = simCore;
    }

    public TicketSellingEvent(double executionTime, TicketSelling simCore) {
        super(executionTime);
        this.simCore = simCore;
    }
}
