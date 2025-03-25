package mpoljak.dsim.assignment_02.logic.sim;

import mpoljak.dsim.common.SimCore;
import mpoljak.dsim.utils.DoubleComp;

public abstract class EventSim extends SimCore {
    private double shiftTime = 100; // millis

    public EventSim(long replicationsCount) {
        super(replicationsCount);
    }

    public double getShiftTime() {
        return this.shiftTime;
    }

    public void setShiftTime(double shiftTime) {
        if (DoubleComp.compare(shiftTime, 0) > -1) // shiftTime >= 0
            this.shiftTime = shiftTime;
    }

    @Override
    protected void beforeSimulation() {
        // todo clear priority queue
        // todo add artificial events to the event calendars
        super.beforeSimulation();
    }

    private class ArtificialEvent extends DiscreteEvent {
        public ArtificialEvent(double executionTime) {
            super(executionTime);
//            vykonanie=simCore.getTime()+shiftTimr
        }
        public void execute() {
//            SimCore.AddEvent(this);
//            Thread.delayOrSleep(sleepTiméaram);
        }
    }
}
