package mpoljak.dsim.assignment_02.logic.ticketSelling.events;

import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSellingSim;

public class CustomerArrival extends TicketSellingEvent {

    public CustomerArrival(double executionTime, int secondaryPriority, TicketSellingSim simCore) {
        super(executionTime, secondaryPriority, simCore);
    }

    public CustomerArrival(double executionTime, TicketSellingSim simCore) {
        super(executionTime, simCore);
    }

    @Override
    public void execute() throws InterruptedException {
        if (this.simCore.isWorkerBusy()) {
            this.simCore.enqueueCustomer();
        }
        else { // worker is free
            this.simCore.setWorkerBusy();
            this.simCore.addToCalendar(new CustomerServiceStart(this.simCore.getSimTime(), this.simCore));
        }
        this.setExecutionTime(this.simCore.getSimTime() + this.simCore.getNextArrivalDuration());
        this.simCore.addToCalendar(this);
    }
}
