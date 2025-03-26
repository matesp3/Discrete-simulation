package mpoljak.dsim.assignment_02.logic.sim;

import mpoljak.dsim.assignment_02.logic.events.DiscreteEvent;
import mpoljak.dsim.common.SimCore;
import mpoljak.dsim.common.SimResults;
import mpoljak.dsim.utils.DoubleComp;

import java.util.concurrent.PriorityBlockingQueue;

public abstract class EventSim extends SimCore {
    private final PriorityBlockingQueue<DiscreteEvent> eventCal;
    private final double maxSimTime;
    private double shiftTime = 5;
    private long sleepTime = 500; // millis
    private double simTime;
    protected String currentEventId = null;

    public EventSim(long replicationsCount, int estCalCapacity, double maxTime) {
        super(replicationsCount);
        this.eventCal = new PriorityBlockingQueue<>(estCalCapacity, new DiscreteEvent.EventComparator());
        this.simTime = 0;
        this.maxSimTime = maxTime;
    }

    /**
     * Adds <code>event</code> to calendar, which is maintaining priorities of events by which they will be executed.
     * @param event to be added to calendar of events
     */
    public void addToCalendar(DiscreteEvent event) {
        if (event == null)
            return;
        this.eventCal.add(event);
    }

    /**
     * @return frequency in [millis] for an artificial event occur
     */
    public double getShiftTime() {
        return this.shiftTime;
    }

    /**
     * @param shiftTime frequency in [millis] for an artificial event occur
     */
    public void setShiftTime(double shiftTime) {
        if (DoubleComp.compare(shiftTime, 0) > -1) // shiftTime >= 0
            this.shiftTime = shiftTime;
    }

    /**
     * @return current time of simulation
     */
    public double getSimTime() {
        return this.simTime;
    }

    @Override
    protected void beforeExperiment() {
        super.beforeExperiment();
        this.eventCal.clear();
        this.addToCalendar(new SystemEvent(0, this));
        this.simTime = 0;
    }

    @Override
    protected void experiment() throws InterruptedException {
        while (!this.isEnded() && !this.eventCal.isEmpty()) {
            this.checkPauseCondition();
            DiscreteEvent event = this.eventCal.take();
            if (DoubleComp.compare(event.getExecutionTime(), this.simTime) == -1) // event.time < this.simTime
                throw new RuntimeException("Time causality violation.");
            if (DoubleComp.compare(event.getExecutionTime(), this.maxSimTime) == 1) // event.getExecutionTime() > this.maxSimTime
                break;
            this.currentEventId = event.getClass().getSimpleName();
            this.simTime = event.getExecutionTime();
            event.execute();
            this.notifyDelegates();
        }
    }

    @Override
    protected SimResults getLastResults() {
        return new SimResults((this.eventCal.size()));
    }

    private static class SystemEvent extends DiscreteEvent {
        private final EventSim simCore;

        public SystemEvent(double executionTime, EventSim simCore) {
            super(executionTime);
            this.simCore = simCore;
        }

        public void execute() throws InterruptedException {
            Thread.sleep(this.simCore.sleepTime);
            this.setExecutionTime(this.simCore.simTime + this.simCore.shiftTime);
            this.simCore.addToCalendar(this);
        }
    }
}
