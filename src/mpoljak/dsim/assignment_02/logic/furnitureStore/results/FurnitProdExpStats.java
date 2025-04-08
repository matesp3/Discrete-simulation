package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.common.SimResults;

import java.util.ArrayList;
import java.util.List;

public class FurnitProdExpStats extends SimResults {
    private List<StatResult> results;
    private double orderTimeInSystemMean;
    private double orderTimeInSystemHValue;

    public FurnitProdExpStats(long experimentNum) {
        super(experimentNum);
        this.results = new ArrayList<StatResult>();
    }

    public void addResult(StatResult result) {
        this.results.add(result);
    }

    public void setResults(List<StatResult> results) {
        this.results = results;
    }

    public void setResult(int index, StatResult result) {
        this.results.set(index, result);
    }

    public List<StatResult> getResults() {
        return this.results;
    }

    public void setOrderTimeInSystem(double mean, double h) {
        this.orderTimeInSystemMean = mean;
        this.orderTimeInSystemHValue = h;
    }

    public double getOrderTimeInSystemMean() {
        return orderTimeInSystemMean;
    }

    public double getOrderTimeInSystemHValue() {
        return orderTimeInSystemHValue;
    }
}
