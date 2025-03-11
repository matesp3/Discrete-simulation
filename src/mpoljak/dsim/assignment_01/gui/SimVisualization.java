package mpoljak.dsim.assignment_01.gui;

import mpoljak.dsim.assignment_01.controllers.SimController;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimVisualization extends JFrame implements ActionListener {
// --- GUI vars
    // constants
    private static final int CANVAS_WIDTH = 1400;
    private static final int CANVAS_HEIGHT = 840;
    private static final int TXT_FIELD_HEIGHT = 22;
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
    private SimulationTask simTask;
    private SimController simController;

    public SimVisualization(SimulationTask simTask) {
        this.simController = new SimController(); // todo: this instance should be passed as constructor param
        this.simTask = simTask;
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
            if (this.simTask.isCancelled())
                this.simTask = this.simTask.cloneInstance();
            this.simTask.execute();
        }
        else if (e.getActionCommand().equals("Stop")) {
            this.simTask.cancel(true);
            this.setBtnEnabled(this.btnStop, false);
            this.setBtnEnabled(this.btnStart,true);
        }
        else if (e.getActionCommand().equals("Show 1 rep")) {
            // todo show 1 replication
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

    private void createFunctionalities() {
//      ---- components
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

        JButton btn1Rep = this.createBtn("Show 1 rep");
        this.northPanel.add(btn1Rep, this.consNorthPanel);
    }
}
