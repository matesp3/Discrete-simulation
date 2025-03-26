package mpoljak.dsim.assignment_02.logic.sim;

import mpoljak.dsim.assignment_02.logic.events.ticketSelling.CustomerArrival;
import mpoljak.dsim.common.SimResults;
import mpoljak.dsim.generators.ExponentialRnd;

public class TicketSelling extends EventSim {
    private int queueLength;
    private boolean workerBusy;
    private ExponentialRnd rndArrivals;
    private ExponentialRnd rndDurations;

    public TicketSelling(long replicationsCount, int estCalCapacity) {
        super(replicationsCount, estCalCapacity, 100000);
        this.workerBusy = false;
        this.queueLength = 0;
        this.rndArrivals = new ExponentialRnd(12 * 60); // 12 per 60 min
        this.rndDurations = new ExponentialRnd(15 * 60); // 15 per 60 min
    }

    public void setWorkerFree() {
        this.workerBusy = false;
    }

    public void setWorkerBusy() {
        this.workerBusy = true;
    }

    public boolean isWorkerBusy() {
        return this.workerBusy;
    }

    public void enqueueCustomer() {
        this.queueLength++;
    }

    public void dequeueCustomer() {
        if (this.queueLength > 0)
            this.queueLength--;
    }

    public int getCustomerQueueLength() {
        return this.queueLength;
    }

    /**
     * @return generated amount of time required to take care of customer by worker
     */
    public double getNextServiceDuration() {
        return this.rndDurations.sample();
    }

    /**
     * @return generated amount of time until new occurrence of customer's arrival
     */
    public double getNextArrivalDuration() {
        return this.rndArrivals.sample();
    }

    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        this.addToCalendar(new CustomerArrival(0, this));
    }

    @Override
    protected SimResults getLastResults() {
        return new SimResults(this.queueLength);
    }
}
