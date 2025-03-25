package mpoljak.dsim.assignment_02.logic.sim;

import mpoljak.dsim.common.Generator;
import mpoljak.dsim.utils.DoubleComp;

public abstract class DiscreteEvent implements Comparable<DiscreteEvent> {
    private final double executionTime;
    private final int secondaryPriority;
    private EventSim sim;
    private Generator gen;

    public DiscreteEvent(double executionTime, int secondaryPriority) {
        this.executionTime = executionTime;
        this.secondaryPriority = secondaryPriority;
    }

    public DiscreteEvent(double executionTime) {
        this(executionTime, -1);
    }

    @Override
    public int compareTo(DiscreteEvent o) {
        int resCmp = DoubleComp.compare(this.executionTime, o.executionTime);
        if (this.secondaryPriority == -1) {
            return resCmp;
        }
        return resCmp == 0 ? Integer.compare(this.secondaryPriority, o.secondaryPriority) : resCmp;
    }

    public abstract void execute();

}
