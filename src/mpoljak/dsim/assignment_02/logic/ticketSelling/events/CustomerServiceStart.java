package mpoljak.dsim.assignment_02.logic.ticketSelling.events;

import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSelling;

public class CustomerServiceStart extends TicketSellingEvent {

    public CustomerServiceStart(double executionTime, int secondaryPriority, TicketSelling simCore) {
        super(executionTime, secondaryPriority, simCore);
    }

    public CustomerServiceStart(double executionTime, TicketSelling simCore) {
        super(executionTime, simCore);
    }

    @Override
    public void execute() throws InterruptedException {
        this.simCore.addToCalendar(
                new CustomerServiceEnd(this.simCore.getSimTime() + this.simCore.getNextServiceDuration(), this.simCore)
        );
    }
}
