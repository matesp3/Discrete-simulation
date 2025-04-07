package mpoljak.dsim.assignment_02.logic;

import mpoljak.dsim.common.SimCore;
import mpoljak.dsim.common.SimResults;
import mpoljak.dsim.utils.DoubleComp;

import java.util.PriorityQueue;
import java.util.Queue;

public abstract class EventSim extends SimCore {
    private final Queue<DiscreteEvent> eventCal;
    private final double maxSimTime;
    private volatile double shiftTime = 5;
    private volatile long sleepTime = 1000; // millis
    private double simTime;
    protected String currentEventId = null;
    private boolean debugMode = false;
    private double cachedShiftTime;
    private long cachedSleepTime;
    private boolean shouldCheckPauseCond = true;

    public EventSim(long replicationsCount, int estCalCapacity, double maxTime) {
        super(replicationsCount);
//        this.eventCal = new PriorityBlockingQueue<>(estCalCapacity, new DiscreteEvent.EventComparator());
        this.eventCal = new PriorityQueue<>(estCalCapacity, new DiscreteEvent.EventComparator());
        this.simTime = 0;
        this.maxSimTime = maxTime;
        this.cachedShiftTime = this.shiftTime;
        this.cachedSleepTime = this.sleepTime;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Adds <code>event</code> to calendar, which is maintaining priorities of events by which they will be executed.
     * @param event to be added to calendar of events
     */
    public void addToCalendar(DiscreteEvent event) {
        if (event == null)
            throw new NullPointerException("Planned event can't be null");
        this.eventCal.add(event);
    }

    /**
     * @return frequency in [millis] for an artificial event occur
     */
    public synchronized double getShiftTime() {
        return this.shiftTime;
    }

    /**
     * @param shiftTime frequency in [millis] for an artificial event occur
     */
    public synchronized void setShiftTime(double shiftTime) {
        if (DoubleComp.compare(shiftTime, 0) > -1) // shiftTime >= 0
            this.shiftTime = shiftTime;
    }

    public synchronized long getSleepTime() {
        return this.sleepTime;
    }

    public synchronized void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * @return current time of simulation
     */
    public double getSimTime() {
        return this.simTime;
    }

    protected abstract void afterEventExecution();

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
            if (this.shouldCheckPauseCond)
                this.checkPauseCondition();
//            DiscreteEvent event = this.eventCal.take();
            DiscreteEvent event = this.eventCal.poll();
            if (DoubleComp.compare(event.getExecutionTime(), this.simTime) == -1) // event.time < this.simTime
                throw new RuntimeException("Time causality violation.");
            if (DoubleComp.compare(event.getExecutionTime(), this.maxSimTime) == 1) // event.getExecutionTime() > this.maxSimTime
                break;
            this.simTime = event.getExecutionTime();
            if (this.debugMode) {
                this.currentEventId = event.getClass().getSimpleName();
                System.out.println(String.format("SimTime=%.02f | executing: %s | [rep:%d]", this.simTime, this.currentEventId, this.getCurrentReplication()));
            }
            event.execute();
            this.afterEventExecution();
        }
    }

    @Override
    protected SimResults getLastResults() {
        return new SimResults((this.eventCal.size()));
    }

    public synchronized void setEnabledMaxSimSpeed(boolean enabled) {
        if (enabled) {
            this.shouldCheckPauseCond = false;
            this.cachedShiftTime = this.shiftTime;
            this.cachedSleepTime = this.sleepTime;
            this.shiftTime = this.isEnded() ? (this.maxSimTime + 1): (this.maxSimTime - this.simTime + 1);
            this.sleepTime = 0;
            return;
        }
        this.shouldCheckPauseCond = true;
        this.shiftTime = this.cachedShiftTime;
        this.sleepTime = this.cachedSleepTime;
    }

    private static class SystemEvent extends DiscreteEvent {
        private final EventSim simCore;

        public SystemEvent(double executionTime, EventSim simCore) {
            super(executionTime);
            this.simCore = simCore;
        }

        public void execute() throws InterruptedException {
            synchronized (this) {
                Thread.sleep(this.simCore.sleepTime);
                this.setExecutionTime(this.simCore.simTime + this.simCore.shiftTime);
            }
            this.simCore.addToCalendar(this);
        }
    }
}
