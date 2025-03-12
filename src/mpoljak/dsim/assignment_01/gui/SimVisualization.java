package mpoljak.dsim.assignment_01.gui;

import mpoljak.dsim.assignment_01.controllers.SimController;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimVisualization extends JFrame implements ActionListener {
// --- GUI vars
    // constants
    private static final int CANVAS_WIDTH = 1800;
    private static final int CANVAS_HEIGHT = 840;
    // colors
    private final Color colBg = new Color(148, 172, 204);
    private final Color colBgContent = new Color(193, 193, 193);
    private final Color colBtn = new Color(81, 128, 197);
    private final Color colBtnFont = new Color(228, 230, 236);
    private final Color colBtnDisabled = new Color(204, 226, 253);
    private final Color colBorder = new Color(152, 215, 232);
    private final Color colTextFont = new Color(3, 2, 108);
    // layout stuff
    private JPanel northPanel;
    private GridBagConstraints consNorthPanel;
    private JPanel centerPanel;
    private GridBagConstraints consCenterPanel;
    // buttons
    private JButton btnStart;
    private JButton btnStop;
    // text fields
    private JTextField txtRepCount;
    private JTextField txtPercentsOmitted;
// --- LOGIC providers vars
    private SimController simController;

    public SimVisualization(SimController controller) {
        this.simController = controller;
//      ---- app icon
//    ImageIcon icon = new ImageIcon(System.getProperty("user.dir")+"/GeoApp_imgs/GeoApp-icon.png");
//    this.setIconImage(icon.getImage());
        this.createMainLayout();
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.setResizable(false); // if I don't want client to resize window
        this.setTitle("MC-simulation of car components costs");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.createFunctionalities();
//      ---- set all visible
//        this.pack(); // probably used when dimensions are not set??
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            this.setBtnEnabled(this.btnStop, true);
            this.setBtnEnabled(this.btnStart,false);
            this.simController.startSimulation();
        }
        else if (e.getActionCommand().equals("Stop")) {
            this.simController.terminateSimulation();
            this.setBtnEnabled(this.btnStop, false);
            this.setBtnEnabled(this.btnStart,true);
        }
        else if (e.getActionCommand().equals("Show 1 rep")) {
            if (!this.centerPanel.getComponent(0).isVisible()) {
                this.centerPanel.getComponent(0).setVisible(true); // sim chart
                this.centerPanel.getComponent(1).setVisible(false); // 1-rep chart
            }
            else {
                this.centerPanel.getComponent(0).setVisible(false); // sim chart
                this.centerPanel.getComponent(1).setVisible(true); // 1-rep chart
            }

        }
    }

    private JButton createBtn(String caption) {
        JButton btn = new JButton(caption);
        btn.setBackground(this.colBtn);
        btn.setActionCommand(caption);
        btn.addActionListener(this);
        btn.setForeground(this.colBtnFont);
//        btn.setBorder(BorderFactory.createLineBorder(this.colBorder));
//        btn.setSize(50, 30);
        return btn;
    }

    private void setBtnEnabled(JButton btn, boolean enabled) {
        btn.setBackground(enabled ? this.colBtn : this.colBtnDisabled);
        btn.setEnabled(enabled);
    }

    private JLabel createLabel(String caption) {
        JLabel label = new JLabel(caption);
        label.setForeground(this.colTextFont);
        return label;
    }

    private JTextField createTextInput(int expectedLettersCount) {
        JTextField txtField = new JTextField();
//        txtField.setSize(new Dimension(width, TXT_FIELD_HEIGHT));
        txtField.setColumns( (int)((5.0/7.0)*expectedLettersCount)+1 );
        return txtField;
    }

    private void createMainLayout() {
        this.setLayout(new BorderLayout());
        this.setBackground(this.colBg);

        this.northPanel = new JPanel();
        GridBagLayout gbLayoutNorth = new GridBagLayout();
        this.northPanel.setLayout(gbLayoutNorth);
        this.consNorthPanel = new GridBagConstraints();
        this.consNorthPanel.insets = new Insets(3, 10, 3, 10);
        this.northPanel.setBackground(this.colBg);

        this.centerPanel = new JPanel(); // content - graphs - here
        GridBagLayout gbLayoutCenter = new GridBagLayout();
        this.centerPanel.setLayout(gbLayoutCenter);
        this.consCenterPanel = new GridBagConstraints();
        this.consCenterPanel.insets = new Insets(3, 10, 3, 10);
        this.centerPanel.setBackground(this.colBgContent);

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    private JComboBox<String> createStrategySelection() {
        String[] comboItems = SimController.getStrategies();
        JComboBox<String> strategiesBox = new JComboBox<>(comboItems);
        strategiesBox.setActionCommand("strategy selection");
        strategiesBox.setForeground(this.colTextFont);
        strategiesBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
                String selectedStrategy = (String) comboBox.getSelectedItem();
                simController.setStrategy(selectedStrategy);
            }
        });
        return strategiesBox;
    }

    private ChartPanel createChartPanel(int width, int height, Color samplesColor, JFreeChart chart) {
        // Anonymous classes: https://www.baeldung.com/java-anonymous-classes
        ChartPanel chartPanel = new ChartPanel(chart) { // inpired by: https://stackoverflow.com/a/49124028
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };
        XYPlot xyPlot1 = (XYPlot) chart.getPlot();
        xyPlot1.setDomainCrosshairVisible(true);    // inspired by: https://stackoverflow.com/a/7208723
        xyPlot1.setRangeCrosshairVisible(true);
        XYItemRenderer renderer1 = xyPlot1.getRenderer();
        if (samplesColor != null)
            renderer1.setSeriesPaint(0, samplesColor);
        NumberAxis domain1 = (NumberAxis) xyPlot1.getDomainAxis();
        domain1.setVerticalTickLabels(true);
        domain1.setAutoRange(true);
        return chartPanel;
    }

    private void createChartArea() {
        int chartXYWidth1 = (int) ((6.0/10)*CANVAS_WIDTH - 20);
        int chartXYHeight1 = 680;
        int chartXYWidth2 = CANVAS_WIDTH - 40 - chartXYWidth1;
        int chartXYHeight2 = 680;
        XYSeriesCollection dataset = this.simController.getSimulationDataset();

        JFreeChart chart = ChartFactory.createScatterPlot("30-weeks average costs", "replications",
                "costs[€]", dataset);
        ChartPanel chartPanel = this.createChartPanel(chartXYWidth1, chartXYHeight1, this.colBtn, chart);

        XYSeriesCollection dataset1Rep = this.simController.get1RepDataset();
        JFreeChart chart1Rep = ChartFactory.createScatterPlot("1. replication - DAILY avg costs", "replications",
                "costs[€]", dataset1Rep);
        ChartPanel chartPanel1Rep = this.createChartPanel(chartXYWidth2, chartXYHeight2, this.colBtn, chart1Rep);

        this.centerPanel.add(chartPanel);
        this.centerPanel.add(chartPanel1Rep);
        chartPanel1Rep.setVisible(false);
    }

    private void createFunctionalities() {
        this.btnStart = this.createBtn("Start");
        this.btnStop = this.createBtn("Stop");

        this.northPanel.add(this.btnStart, this.consNorthPanel);
        this.northPanel.add(this.btnStop, this.consNorthPanel);
        this.setBtnEnabled(this.btnStop, false);

        JLabel lblRepCount = this.createLabel("Replications:");
        this.northPanel.add(lblRepCount, this.consNorthPanel);

        JTextField inputRepCount = this.createTextInput(10);
        this.northPanel.add(inputRepCount, this.consNorthPanel);

        JLabel lblStrategies = this.createLabel("Strategies selection:");
        this.northPanel.add(lblStrategies, this.consNorthPanel);

        JComboBox<String> strategiesBox = this.createStrategySelection();
        this.northPanel.add(strategiesBox, this.consNorthPanel);

        JCheckBox check1Rep = new JCheckBox("Show 1 rep");
        check1Rep.setSelected(false);
        check1Rep.addActionListener(e -> {
            this.centerPanel.getComponent(1).setVisible(check1Rep.isSelected());
        });
        this.northPanel.add(check1Rep, this.consNorthPanel);

        JLabel labelPercOmitted = this.createLabel("Replications omitted [%]:");
        this.northPanel.add(labelPercOmitted, this.consNorthPanel);

        JTextField inputPercOmitted = this.createTextInput(3);
        this.northPanel.add(inputPercOmitted, this.consNorthPanel);

        this.createChartArea();
    }
}
