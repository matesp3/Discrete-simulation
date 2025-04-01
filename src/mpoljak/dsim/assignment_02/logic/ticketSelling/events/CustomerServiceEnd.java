package mpoljak.dsim.assignment_02.logic.ticketSelling.events;

import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSellingSim;

public class CustomerServiceEnd extends TicketSellingEvent {

    public CustomerServiceEnd(double executionTime, int secondaryPriority, TicketSellingSim simCore) {
        super(executionTime, secondaryPriority, simCore);
    }

    public CustomerServiceEnd(double executionTime, TicketSellingSim simCore) {
        super(executionTime, simCore);
    }

    @Override
    public void execute() throws InterruptedException {
        if (this.simCore.getCustomerQueueLength() > 0) {
            this.simCore.dequeueCustomer();
            this.simCore.addToCalendar(new CustomerServiceStart(this.simCore.getSimTime(), this.simCore));
        }
        else // there is no queue anymore
            this.simCore.setWorkerFree();
    }
}
