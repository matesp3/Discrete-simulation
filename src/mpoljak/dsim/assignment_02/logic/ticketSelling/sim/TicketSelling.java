package mpoljak.dsim.assignment_02.logic.ticketSelling.sim;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.ticketSelling.events.CustomerArrival;
import mpoljak.dsim.common.SimResults;
import mpoljak.dsim.generators.ExponentialRnd;

public class TicketSelling extends EventSim {
    private int queueLength;
    private boolean workerBusy;
    private final ExponentialRnd rndArrivals;
    private final ExponentialRnd rndDurations;

    public TicketSelling(long replicationsCount, int estCalCapacity) {
        super(replicationsCount, estCalCapacity, 100000);
        this.workerBusy = false;
        this.queueLength = 0;

        this.rndArrivals = new ExponentialRnd(12); // 12 per 60 min  -> 1 per 5 minutes
        this.rndDurations = new ExponentialRnd(15); // 15 per 60 min -> 1 per 4 minutes

        double minDuration = 15; // minutes
        this.setShiftTime(minDuration);
        this.setSleepTime(500);
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
        this.queueLength = 0;
        this.workerBusy = false;
        this.addToCalendar(new CustomerArrival(0, this));
    }

    @Override
    protected SimResults getLastResults() {
        TicketSellRes res = new TicketSellRes(this.getCurrentReplication());
        res.eventId = this.currentEventId;
        res.time = this.getSimTime();
        res.workerBusy = this.workerBusy;
        res.queueLength = this.queueLength;
        return res;
    }

    public static class TicketSellRes extends SimResults {
        private int queueLength;
        private boolean workerBusy;
        private String eventId;
        private double time;

        public TicketSellRes(long replication) {
            super(replication);
        }

        public int getQueueLength() {
            return queueLength;
        }

        public boolean isWorkerBusy() {
            return workerBusy;
        }

        public void setWorkerBusy(boolean workerBusy) {
            this.workerBusy = workerBusy;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }
}
