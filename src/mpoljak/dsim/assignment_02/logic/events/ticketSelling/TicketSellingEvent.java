package mpoljak.dsim.assignment_02.logic.events.ticketSelling;

import mpoljak.dsim.assignment_02.logic.events.DiscreteEvent;
import mpoljak.dsim.assignment_02.logic.sim.TicketSelling;

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
