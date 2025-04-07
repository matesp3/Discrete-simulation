package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.controllers.FurnitProdSimController;
import mpoljak.dsim.assignment_02.gui.components.*;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdEventResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdExpStats;
import mpoljak.dsim.common.ISimDelegate;
import mpoljak.dsim.common.SimResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FurnitureProdForm extends JFrame implements ISimDelegate, ActionListener, ComponentListener {
    // look & feel
    final static String LOOK_AND_FEEL = "Metal"; // null (use the default), "Metal", "System", "Motif" and "GTK"
    final static String THEME = "Test"; // For Metal L&F, themes: "DefaultMetal", "Ocean",  and "Test"
    // colors
    public static final Color COL_BG = new Color(191, 201, 224);
    public static final Color COL_BG_TAB = new Color(223, 227, 238);
    public static final Color COL_BG_CONTENT = new Color(193, 193, 193);
    public static final Color COL_BTN = new Color(81, 128, 197);
    public static final Color COL_BTN_FONT = new Color(255, 255, 255);
    public static final Color COL_BTN_DISABLED = new Color(204, 226, 253);
    public static final Color COL_BORDER = new Color(152, 215, 232);
    public static final Color COL_TEXT_FONT_1 = new Color(3, 2, 108);
    public static final Color COL_TEXT_FONT_2 = new Color(18, 129, 248);
    // layout
    private final JPanel mainPane = new JPanel();
    private final JPanel northPane = new JPanel(); // NORTH
    private final JPanel eastPane = new JPanel(); // EAST
    private final JTabbedPane tabbedContentPane = new JTabbedPane(); // CENTER
    // tabs
    private StatsViewer statsViewer;
    private FurnitureProdAnim animationViewer;
    // custom components
    private InputWithLabel inputA;
    private InputWithLabel inputB;
    private InputWithLabel inputC;
    private InputWithLabel inputExperiments;
    private InputWithLabel inputSimDur;
    private ResultViewer replicationViewer;
    private TimeSlider timeSlider;
    // JComponents
    private JButton btnStart;
    private JButton btnPause;
    private JButton btnCancel;
    private JButton btnSleepConfig;
    private JButton btnShiftConfig;
    private JCheckBox checkLogs;
    private JCheckBox checkMaxSpeed;

    private final FurnitProdSimController furnitProdSimController;
    private boolean simPaused;

    public FurnitureProdForm() {
//        ---- initialization of params of business logic
        this.furnitProdSimController = new FurnitProdSimController(this);
        this.simPaused = false;
//        ---- window: size, layout and behavior
        this.setSize(new Dimension(1500,700));
        this.setMinimumSize(new Dimension(1300, 500));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(this.mainPane);
        this.mainPane.setBackground(COL_BG);
        this.mainPane.setSize(this.getWidth(), this.getHeight());
        this.createLayout();
//        ----- window: components
        this.createComponents();
//        ----- window: go live
        this.setVisible(true);
        this.addComponentListener(this);
    }



    @Override
    public void update(SimResults res) {
        if (!this.checkMaxSpeed.isSelected() && res instanceof FurnitProdEventResults) {
            SwingUtilities.invokeLater(() -> {
                this.animationViewer.setEventResultsModel( (FurnitProdEventResults)res );
            });
        }
        else if (res instanceof FurnitProdExpStats)
            SwingUtilities.invokeLater(() -> {
                FurnitProdExpStats r = (FurnitProdExpStats)res;
                this.statsViewer.updateStats(r.getResults());
                this.replicationViewer.setValue(r.getExperimentNum());
            });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Run")) {
            if (! this.furnitProdSimController.isSimRunning()) {
                this.statsViewer.clearStatsList();
                this.furnitProdSimController.launchSimulation(this.inputA.getIntValue(), this.inputB.getIntValue(),
                        this.inputC.getIntValue(), this.inputExperiments.getIntValue(), this.inputSimDur.getDoubleValue());
                this.setBtnEnabled(this.btnStart, false);
                this.setBtnEnabled(this.btnPause, true);
                this.setBtnEnabled(this.btnCancel, true);
                this.setEnabledInputs(false);
                this.replicationViewer.setValue(0);
            }
        }
        else if (cmd.equals("Cancel")) {
            this.furnitProdSimController.terminateSimulation();
            this.onSimEnd();
        }
        else if (cmd.equals("Pause")) {
            if (this.simPaused) {
                this.furnitProdSimController.resumeSimulation();
                this.btnPause.setText("Pause");
            } else {
                this.furnitProdSimController.pauseSimulation();
                this.btnPause.setText("Resume");
            }
            this.simPaused = !this.simPaused;
        }
        else if (cmd.equals("Max-speed")) {
            this.furnitProdSimController.setEnabledMaxSpeed(checkMaxSpeed.isSelected());
            checkLogs.setEnabled(!checkMaxSpeed.isSelected());
            this.timeSlider.setVisible(!checkMaxSpeed.isSelected());
            if (checkMaxSpeed.isSelected()) {
                checkLogs.setSelected(false);
                furnitProdSimController.setEnabledConsoleLogs(false);
            }
        }
        else if (cmd.equals("Console-logs")) {
            this.furnitProdSimController.setEnabledConsoleLogs(checkLogs.isSelected());
        }
    }

    private void onSimEnd() {
        this.setBtnEnabled(this.btnStart, true);
        this.setBtnEnabled(this.btnPause, false);
        this.setBtnEnabled(this.btnCancel, false);
        this.setEnabledInputs(true);
        this.btnPause.setText("Pause");
        this.simPaused = false;
    }

    private void createLayout() {
        this.mainPane.setLayout(new BorderLayout());
        this.mainPane.add(this.northPane, BorderLayout.NORTH);
        this.mainPane.add(this.eastPane, BorderLayout.EAST);
        this.mainPane.add(this.tabbedContentPane, BorderLayout.CENTER);
        this.eastPane.setBackground(COL_BG);
        this.eastPane.setLayout(new BoxLayout(eastPane, BoxLayout.Y_AXIS));
        this.eastPane.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        this.northPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.northPane.setBorder(BorderFactory.createLineBorder(new Color(17, 79, 94)));
        this.northPane.setBackground(COL_BG);
        this.tabbedContentPane.setBackground(COL_BG);
    }

    private void createTabs() {
        this.statsViewer = new StatsViewer();
        this.animationViewer = new FurnitureProdAnim();

        this.tabbedContentPane.addTab("Statistics", this.statsViewer);
        this.tabbedContentPane.addTab("Animation", this.animationViewer);
        this.tabbedContentPane.addTab("Value Stabilization", new JLabel("*** TODO chart"));
    }

    private void createNorthPart() {
        this.timeSlider = new TimeSlider(this.furnitProdSimController, true, COL_BG);
        this.northPane.add(this.timeSlider);

        this.replicationViewer = new ResultViewer("Executed replications");
        this.replicationViewer.setBorder(BorderFactory.createRaisedBevelBorder());
        this.northPane.add(Box.createRigidArea(new Dimension(50, 0)));
        this.northPane.add(this.replicationViewer);
    }

    private void createEastPart() {
        JPanel checks = new JPanel();
        checks.setLayout(new BoxLayout(checks, BoxLayout.Y_AXIS));
        checks.setBackground(COL_BG);

        this.checkLogs = new JCheckBox("Console-logs");
        checkLogs.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkLogs.setBackground(COL_BG);
        checkLogs.addActionListener(this);
        checkLogs.setSelected(false);
        checks.add(checkLogs);
        this.furnitProdSimController.setEnabledConsoleLogs(checkLogs.isSelected());

        this.checkMaxSpeed = new JCheckBox("Max-speed");
        checkMaxSpeed.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkMaxSpeed.setBackground(COL_BG);
        checkMaxSpeed.addActionListener(this);
        checkMaxSpeed.setSelected(false);
        this.furnitProdSimController.setEnabledMaxSpeed(checkMaxSpeed.isSelected());

        checks.add(Box.createRigidArea(new Dimension(0, 5)));
        checks.add(checkMaxSpeed);
        checks.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        this.eastPane.add(checks);

        this.btnStart = this.createBtn("Run");
        this.btnPause = this.createBtn("Pause");
        this.btnCancel = this.createBtn("Cancel");
        this.btnSleepConfig = this.createBtn("Config sleep");
        this.btnShiftConfig = this.createBtn("Config shift");

        this.setBtnEnabled(btnPause, false);
        this.setBtnEnabled(btnCancel, false);
        // - - - - - - - - -
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(COL_BG);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        this.btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.btnPause.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Fixed vertical space
        buttonPanel.add(this.btnStart);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Fixed vertical space
        buttonPanel.add(this.btnPause);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Fixed vertical space
        buttonPanel.add(this.btnCancel);
//        buttonPanel.add(Box.createVerticalGlue());
        this.eastPane.add(Box.createRigidArea(new Dimension(0, 10))); // Fixed vertical space
        this.eastPane.add(buttonPanel);
        // - - - - - - - - -
        JPanel inputsPanel = new JPanel();
        inputsPanel.setLayout(new BoxLayout(inputsPanel, BoxLayout.Y_AXIS));
//        inputsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        inputsPanel.setBorder(BorderFactory.createSoftBevelBorder(0, new Color(193, 239, 255), COL_BTN));
        inputsPanel.setBackground(COL_BG);

        this.inputA = new InputWithLabel("Amount A:", 2, "2");
        this.inputB = new InputWithLabel("Amount B:", 2, "3");
        this.inputC = new InputWithLabel("Amount C:", 2, "20");
        this.inputSimDur = new InputWithLabel("Dur [days]:", 3, "249");
        this.inputExperiments = new InputWithLabel("Experiments:", 6, "10000");
        JLabel lblConf = new JLabel("Params:");
        inputsPanel.add(lblConf);
        inputsPanel.add(Box.createRigidArea(new Dimension(0, 7))); // Fixed vertical space
        inputsPanel.add(this.inputA);
        inputsPanel.add(this.inputB);
        inputsPanel.add(this.inputC);
        inputsPanel.add(this.inputSimDur);
        inputsPanel.add(Box.createRigidArea(new Dimension(0, 3))); // Fixed vertical space
        inputsPanel.add(this.inputExperiments);
        this.eastPane.add(Box.createRigidArea(new Dimension(0, 20)));
        this.eastPane.add(inputsPanel);
//      ---------------------------------------
//        JPanel conf1 = new JPanel();
//        conf1.setBackground(COL_BG);
//        JTextField txtSleep = this.createTextInput(6, "1000");
//        this.btnSleepConfig.addActionListener(e -> {simController.setSleepTime(Integer.parseInt(txtSleep.getText()));});
//        conf1.add(this.btnSleepConfig);
//        conf1.add(txtSleep);
//
//        JPanel conf2 = new JPanel();
//        conf2.setBackground(COL_BG);
//        JTextField txtShift = this.createTextInput(6, "5");
//        this.btnShiftConfig.addActionListener(e -> {simController.setShiftTime(Integer.parseInt(txtShift.getText()));});
//        conf2.add(this.btnShiftConfig);
//        conf2.add(txtShift);
    }

    private void createComponents() {
        this.createTabs();
        this.createNorthPart();
        this.createEastPart();
    }

    private JButton createBtn(String caption) {
        JButton btn = new JButton(caption);
        btn.setBackground(COL_BTN);
        btn.setActionCommand(caption);
        btn.addActionListener(this);
        btn.setForeground(COL_BTN_FONT);
//        btn.setBorder(BorderFactory.createLineBorder(this.colBorder));
//        btn.setSize(50, 30);
        return btn;
    }

    private void setBtnEnabled(JButton btn, boolean enabled) {
        btn.setBackground(enabled ? COL_BTN : COL_BTN_DISABLED);
        btn.setEnabled(enabled);
    }

    private void setEnabledInputs(boolean enabled) {
        this.inputExperiments.setEnabled(enabled);
        this.inputA.setEnabled(enabled);
        this.inputB.setEnabled(enabled);
        this.inputC.setEnabled(enabled);
        this.inputSimDur.setEnabled(enabled);

        this.inputExperiments.setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputA.setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputB.setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputC.setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputSimDur.setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputExperiments.getParent().setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
        this.inputA.getParent().setBackground(enabled ? COL_BG : COL_BTN_DISABLED);
    }

    private JLabel createLabel(String caption) {
        JLabel label = new JLabel(caption);
        label.setForeground(COL_TEXT_FONT_1);
        return label;
    }

    private JTextField createTextInput(int expectedLettersCount, String hintText) {
        JTextField txtField = new JTextField(hintText);
//        txtField.setSize(new Dimension(width, TXT_FIELD_HEIGHT));
        txtField.setColumns( (int)((5.0/7.0)*expectedLettersCount)+1 );
        return txtField;
    }

    @Override
    public void componentResized(ComponentEvent e) {
//        System.out.println("resized");
        this.animationViewer.resizeContent(this.getWidth() - 200, this.getHeight() - 150);
        this.statsViewer.resizeContent(this.getWidth() - 200, this.getHeight() - 150);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
