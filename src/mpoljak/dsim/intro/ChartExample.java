package mpoljak.dsim.intro;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class ChartExample {
    public static void main(String[] args) {
        /* TYPE1: line & bar chart */
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(200, "Costs", "1");
//        dataset.addValue(150, "Costs", "2");
//        dataset.addValue(180, "Costs", "3");
//        dataset.addValue(260, "Costs", "4");
//        dataset.addValue(300, "Costs", "5");
//        dataset.addValue(250, "Costs", "6");
//        dataset.addValue(200, "Costs", "1");
//        dataset.addValue(150, "Costs", "2");
//        dataset.addValue(180, "Costs", "3");
//        dataset.addValue(260, "Costs", "4");
//        dataset.addValue(300, "Costs", "5");
//        dataset.addValue(250, "Costs", "6");
//        JFreeChart chart = ChartFactory.createLineChart("Title: Average costs", "replication", "cost[€]",
//                null, PlotOrientation.VERTICAL, true, true, false);
//        JFreeChart chart = ChartFactory.createLineChart("Monthly sales", "Month", "Sales", dataset);
        /* TYPE2: Pie chart */
//        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
//        dataset.setValue("January", 200);
//        dataset.setValue("February",150);
//        dataset.setValue("March", 180);
//        JFreeChart chart = ChartFactory.createPieChart("Monthly Sales", dataset, true, true, false);
        /* TYPE3: Time Series Chart */
//        TimeSeries series = new TimeSeries("Monthly sales");
//        series.add(new Month(1, 2024), 200);
//        series.add(new Month(2, 2024), 150);
//        series.add(new Month(3, 2024), 180);
//        series.addAndOrUpdate()
//        TimeSeriesCollection dataset = new TimeSeriesCollection();
//        dataset.addSeries(series);
//        JFreeChart chart = ChartFactory.createTimeSeriesChart("Monthly Sales", "Date", "Sales", dataset, true, false, false);
        /* TYPE4: XY plot */
        // https://coderanch.com/t/674627/java/Arraylist-xyseries-Jfreechart
        XYSeries xy = new XYSeries("Nazov");
        xy.add(100, 5.5);
        xy.add(200, 6.5);
        xy.add(300, 7.5);
        xy.add(400, 8.5);
        // https://coderslegacy.com/java/jfreechart-scatter-plot/
        XYSeriesCollection dataset = new XYSeriesCollection(xy);

        JFreeChart chart = ChartFactory.createScatterPlot("nazov", "hodnota-x", "hodnota-y", dataset);
        // displaying
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setContentPane(chartPanel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
