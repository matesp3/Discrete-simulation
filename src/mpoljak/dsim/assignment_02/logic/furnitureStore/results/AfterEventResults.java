package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.common.SimResults;

public class AfterEventResults extends SimResults {
    private double simTime;

    public AfterEventResults(long experimentNum, double simTime) {
        super(experimentNum);
        this.simTime = simTime;
    }

    public double getSimTime() {
        return simTime;
    }

    public void setSimTime(double simTime) {
        this.simTime = simTime;
    }
}
