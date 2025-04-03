package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.common.SimResults;

import java.util.ArrayList;
import java.util.List;

public class FurnitProdExpStats extends SimResults {
    private List<StatResult> results;
    private double countInOrderSystemMean;
    private double countInOrderSystemHValue;
    private double countStainingMean;
    private double countStainingHValue;
    private double countAssemblingMean;
    private double countAssemblingHValue;
    private double countFitInstallingMean;
    private double countFitInstallingHValue;

    private double timeInOrderSystemMean;
    private double timeInOrderSystemHValue;
    private double timeStainingMean;
    private double timeStainingHValue;
    private double timeAssemblingMean;
    private double timeAssemblingHValue;
    private double timeFitInstallingMean;
    private double timeFitInstallingHValue;

    private double utilizationMeanA;
    private double utilizationHValueA;
    private double utilizationMeanB;
    private double utilizationHValueB;
    private double utilizationMeanC;
    private double utilizationHValueC;

    private double timeInSystemMean;
    private double timeInSystemHValue;

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

//      -   -   -   - setters:

    public void setCountInOrderSystem(double mean, double h) {
        this.countInOrderSystemMean = mean;
        this.countInOrderSystemHValue = h;
    }

    public void setCountStainingSystem(double mean, double h) {
        this.countStainingMean = mean;
        this.countStainingHValue = h;
    }

    public void setCountAssemblingSystem(double mean, double h) {
        this.countAssemblingMean = mean;
        this.countAssemblingHValue = h;
    }

    public void setCountFitInstallingSystem(double mean, double h) {
        this.countFitInstallingMean = mean;
        this.countFitInstallingHValue = h;
    }

    public void setTimeInOrderSystem(double mean, double h) {
        this.timeInOrderSystemMean = mean;
        this.timeInOrderSystemHValue = h;
    }

    public void setTimeStainingSystem(double mean, double h) {
        this.timeStainingMean = mean;
        this.timeStainingHValue = h;
    }

    public void setTimeAssemblingSystem(double mean, double h) {
        this.timeAssemblingMean = mean;
        this.timeAssemblingHValue = h;
    }

    public void setTimeFitInstallingSystem(double mean, double h) {
        this.timeFitInstallingMean = mean;
        this.timeFitInstallingHValue = h;
    }

    public void setUtilizationOfA(double mean, double h) {
        this.utilizationMeanA = mean;
        this.utilizationHValueA = h;
    }

    public void setUtilizationOfB(double mean, double h) {
        this.utilizationMeanB = mean;
        this.utilizationHValueB = h;
    }

    public void setUtilizationOfCM(double mean, double h) {
        this.utilizationMeanC = mean;
        this.utilizationHValueC = h;
    }

    public void setTimeInSystem(double mean, double h) {
        this.timeInSystemMean = mean;
        this.timeInSystemHValue = h;
    }
//    - -   -   -   getters:

    public double getCountInOrderSystemMean() {
        return countInOrderSystemMean;
    }

    public double getCountInOrderSystemHValue() {
        return countInOrderSystemHValue;
    }

    public double getCountStainingMean() {
        return countStainingMean;
    }

    public double getCountStainingHValue() {
        return countStainingHValue;
    }

    public double getCountAssemblingMean() {
        return countAssemblingMean;
    }

    public double getCountAssemblingHValue() {
        return countAssemblingHValue;
    }

    public double getCountFitInstallingMean() {
        return countFitInstallingMean;
    }

    public double getCountFitInstallingHValue() {
        return countFitInstallingHValue;
    }

    public double getTimeInOrderSystemMean() {
        return timeInOrderSystemMean;
    }

    public double getTimeInOrderSystemHValue() {
        return timeInOrderSystemHValue;
    }

    public double getTimeStainingMean() {
        return timeStainingMean;
    }

    public double getTimeStainingHValue() {
        return timeStainingHValue;
    }

    public double getTimeAssemblingMean() {
        return timeAssemblingMean;
    }

    public double getTimeAssemblingHValue() {
        return timeAssemblingHValue;
    }

    public double getTimeFitInstallingMean() {
        return timeFitInstallingMean;
    }

    public double getTimeFitInstallingHValue() {
        return timeFitInstallingHValue;
    }

    public double getUtilizationMeanA() {
        return utilizationMeanA;
    }

    public double getUtilizationHValueA() {
        return utilizationHValueA;
    }

    public double getUtilizationMeanB() {
        return utilizationMeanB;
    }

    public double getUtilizationHValueB() {
        return utilizationHValueB;
    }

    public double getUtilizationMeanC() {
        return utilizationMeanC;
    }

    public double getUtilizationHValueC() {
        return utilizationHValueC;
    }

    public double getTimeInSystemMean() {
        return timeInSystemMean;
    }

    public double getTimeInSystemHValue() {
        return timeInSystemHValue;
    }
}
