package mpoljak.dsim.assignment_02.logic.sim;

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
            if (o1.secondaryPriority == -1) {
                return resCmp;
            }
            return resCmp == 0 ? Integer.compare(o1.secondaryPriority, o2.secondaryPriority) : resCmp;
        }
    }

    private final double executionTime;
    private final int secondaryPriority;
    private EventSim sim;
    private Generator gen;

    public DiscreteEvent(double executionTime, int secondaryPriority) {
        this.executionTime = executionTime;
        this.secondaryPriority = secondaryPriority;
    }

    public double getExecutionTime() {
        return this.executionTime;
    }

    /**
     * @return secondary priority (no negative number) or -1, if it's not used in event's context
     */
    public int getSecondaryPriority() {
        return this.secondaryPriority;
    }

    public DiscreteEvent(double executionTime) {
        this(executionTime, -1);
    }

    public abstract void execute();

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
        @Override
        public void execute() {}
        @Override
        public String toString() {
            return String.format("tim=%d", (int)this.getExecutionTime() );
        }
    }
}
