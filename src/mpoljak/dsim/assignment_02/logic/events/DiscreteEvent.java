package mpoljak.dsim.assignment_02.logic.events;

import mpoljak.dsim.assignment_02.logic.sim.EventSim;
import mpoljak.dsim.common.Generator;
import mpoljak.dsim.generators.DiscreteUniformRnd;
import mpoljak.dsim.utils.DoubleComp;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public abstract class DiscreteEvent {

    public static class EventComparator implements Comparator<DiscreteEvent> {
        @Override
        public int compare(DiscreteEvent o1, DiscreteEvent o2) {
            int resCmp = DoubleComp.compare(o1.executionTime, o2.executionTime);
            if (o1.secondaryPriority < 0 && o2.secondaryPriority < 0) {
                return resCmp;
            }
            if (o1.secondaryPriority < 0) // o2 has higher priority
                return 1;
            if (o2.secondaryPriority < 0) // o1 has higher priority
                return -1;
            return resCmp == 0 ? Integer.compare(o1.secondaryPriority, o2.secondaryPriority) : resCmp;
        }
    }

//    protected EventSim simCore;
    private final int secondaryPriority;
    private double executionTime;

    public DiscreteEvent(double executionTime, int secondaryPriority) {
//    public DiscreteEvent(double executionTime, int secondaryPriority, EventSim simCore) {
        this.executionTime = executionTime;
        this.secondaryPriority = secondaryPriority;
//        this.simCore = simCore;
    }

    public DiscreteEvent(double executionTime) {
//    public DiscreteEvent(double executionTime, EventSim simCore) {
        this(executionTime, -1);
    }

    public double getExecutionTime() {
        return this.executionTime;
    }

    /**
     * Sets new time of event execution.
     * @param executionTime new time of event execution
     */
    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    /**
     * @return secondary priority (no negative number) or -1, if it's not used in event's context
     */
    public int getSecondaryPriority() {
        return this.secondaryPriority;
    }

    public abstract void execute() throws InterruptedException;

//  -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -   -
    public static void main(String[] args) throws InterruptedException {
        Generator rnd = new DiscreteUniformRnd(1,20);
        PriorityBlockingQueue<DiscreteEvent> queue =
                new PriorityBlockingQueue<>(2, new DiscreteEvent.EventComparator());
        System.out.println("Insertions:");
        for (int i = 0; i < 5; i++) {
            double sample = rnd.sample();
            System.out.println("add: sample="+sample);
            queue.add(new TestEvent(sample));
            System.out.println("    |_-->"+queue);
        }
        System.out.println("\nPolls:");
        for (int i = 0; i < 5; i++) {
            System.out.println("take: " + queue.take());
            System.out.println("    |_-->"+queue);
        }
    }

    public static class TestEvent extends DiscreteEvent {

        public TestEvent(double executionTime) {
            super(executionTime);
        }

        public TestEvent(double executionTime, int secondaryPriority) {
            super(executionTime, secondaryPriority);
        }
        @Override
        public void execute() {}
        @Override
        public String toString() {
            return String.format("tim=%d sec=%d", (int)this.getExecutionTime(), this.getSecondaryPriority() );
        }
    }
}
