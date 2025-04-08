package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.utils.DoubleComp;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class CIChartViewer extends JPanel {
    private final XYSeries seriesLowerCIBound;
    private final XYSeries seriesMean;
    private final XYSeries seriesUpperCIBound;
    private ChartPanel chartPanel;
    private InputWithLabel inputPercOmit;
    private ResultViewer resultLowerBound;
    private ResultViewer resultMean;
    private ResultViewer resultUpperBound;

    private long nthVal;
    private long maxReplicationNr;
    private long omittedReps;

    public CIChartViewer() {
        this.nthVal = 30;
        this.seriesLowerCIBound = new XYSeries("Low CI bound");
        this.seriesMean = new XYSeries("Mean");
        this.seriesUpperCIBound = new XYSeries("High CI bound");
        this.createComponents();
    }

    public void resizeContent(int width, int height) {
        this.chartPanel.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Adds new value into the chart displaying mean and its corresponding confidence interval bounds.
     * @param experimentNr number of experiment (replication)
     * @param mean value of mean
     * @param h value of half width of confidence interval width
     */
    public void addValue(double experimentNr, double mean, double h) {
        if (experimentNr > this.omittedReps && experimentNr % this.nthVal == 0) {
            this.seriesLowerCIBound.add(experimentNr, mean - h);
            this.seriesMean.add(experimentNr, mean);
            this.seriesUpperCIBound.add(experimentNr, mean + h);
            this.resultLowerBound.setValue(mean-h,5);
            this.resultMean.setValue(mean,5);
            this.resultUpperBound.setValue(mean+h,5);
        }
    }

    /**
     * Removes all values from chart and also sets number of omitted replications specified from user input field.
     */
    public void clearChart() {
        this.seriesLowerCIBound.clear();
        this.seriesMean.clear();
        this.seriesUpperCIBound.clear();
        this.updatePercentageOmission();
    }

    /**
     * @return max number of inserted replications to chart
     */
    public long getMaxReplicationNr() {
        return this.maxReplicationNr;
    }

    /**
     * @param maxReplicationNr max number of inserted replications to chart
     */
    public void setMaxReplicationNr(long maxReplicationNr) {
        this.maxReplicationNr = maxReplicationNr;
    }

    private void createComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        seriesCollection.addSeries(this.seriesLowerCIBound);
        seriesCollection.addSeries(this.seriesMean);
        seriesCollection.addSeries(this.seriesUpperCIBound);

        this.inputPercOmit = new InputWithLabel("Omitted replications [in %]", 4, "5");
        this.inputPercOmit.setBorder(BorderFactory.createRaisedBevelBorder());
        this.inputPercOmit.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.resultLowerBound = new ResultViewer("CI - low bound", "");
        this.resultLowerBound.setBorder(BorderFactory.createRaisedBevelBorder());
        this.resultLowerBound.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.resultMean = new ResultViewer("mean", "");
        this.resultMean.setBorder(BorderFactory.createRaisedBevelBorder());
        this.resultMean.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.resultUpperBound = new ResultViewer("CI - upper bound", "");
        this.resultUpperBound.setBorder(BorderFactory.createRaisedBevelBorder());
        this.resultUpperBound.setBackground(FurnitureProdForm.COL_BG_TAB);

//        JFreeChart chart = ChartFactory.createScatterPlot("Order's time in system stabilization", "replications",
        JFreeChart chart = ChartFactory.createXYLineChart("Order's time in system stabilization", "replications",
                "time in system [h]", seriesCollection);
        this.chartPanel = this.createChartPanel(chart);

        JPanel p0 = new JPanel();
        p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));
        p0.add(this.inputPercOmit);
        p0.add(this.resultLowerBound);
        p0.add(this.resultMean);
        p0.add(this.resultUpperBound);

        this.add(p0);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(this.chartPanel);

    }

    private ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);    // inspired by: https://stackoverflow.com/a/7208723
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(1, new BasicStroke(3.0f));
        renderer.setSeriesPaint(1, new Color(6, 89, 32));
        renderer.setSeriesStroke(2, new BasicStroke(3.0f));
        renderer.setSeriesPaint(2, Color.RED);

        NumberAxis domainX = (NumberAxis) xyPlot.getDomainAxis();
        domainX.setVerticalTickLabels(true);
        domainX.setAutoRange(true);
        domainX.setAutoRangeIncludesZero(false);

        NumberAxis domainY = (NumberAxis) xyPlot.getRangeAxis();
        domainY.setVerticalTickLabels(false);
        domainY.setAutoRange(true);
        domainY.setAutoRangeIncludesZero(false);

        return chartPanel;
    }

    private void updatePercentageOmission() {
        this.omittedReps = (int)( this.maxReplicationNr * (this.inputPercOmit.getDoubleValue()/100.0));
        this.nthVal = DoubleComp.compare((this.maxReplicationNr-this.omittedReps)/1000.0, 1.0) == -1
                ? 1 : ((this.maxReplicationNr - omittedReps) /1000);
    }
}
