package mpoljak.dsim.intro;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;

public class ChartExample {
    public static void main(String[] args) {
        /* line & bar chart */
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(200, "Sales", "January");
//        dataset.addValue(150, "Sales", "February");
//        dataset.addValue(180, "Sales", "March");
//        dataset.addValue(260, "Sales", "April");
//        dataset.addValue(300, "Sales", "May");
//        dataset.addValue(250, "Sales", "June");
//        JFreeChart chart = ChartFactory.createLineChart("Monthly sales", "Month", "Sales", dataset);
//        JFreeChart chart = ChartFactory.createBarChart("Monthly sales", "Month", "Sales", dataset);
        /* Pie chart */
//        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
//        dataset.setValue("January", 200);
//        dataset.setValue("February",150);
//        dataset.setValue("March", 180);
//        JFreeChart chart = ChartFactory.createPieChart("Monthly Sales", dataset, true, true, false);
        /* Time Series Chart */
        TimeSeries series = new TimeSeries("Monthly sales");
        series.add(new Month(1, 2024), 200);
        series.add(new Month(2, 2024), 150);
        series.add(new Month(3, 2024), 180);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Monthly Sales", "Date", "Sales", dataset, true, false, false);
        // displaying
        ChartPanel chartPanel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.setContentPane(chartPanel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //todo urobit okno s kontajnerom, ktory ma vnoreny graf a bude to interaktivne na pridavanie cisiel
    }
}
