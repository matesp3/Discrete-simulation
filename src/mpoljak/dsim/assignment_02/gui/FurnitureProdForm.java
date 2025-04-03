package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.controllers.SimController;
import mpoljak.dsim.assignment_02.gui.components.InputWithLabel;
import mpoljak.dsim.assignment_02.gui.components.OverallStatsViewer;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdExpStats;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;
import mpoljak.dsim.common.ISimDelegate;
import mpoljak.dsim.common.SimResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FurnitureProdForm extends JFrame implements ISimDelegate, ActionListener {

    // colors
    public static final Color COL_BG = new Color(191, 201, 224);
    public static final Color COL_BG_CONTENT = new Color(193, 193, 193);
    public static final Color COL_BTN = new Color(81, 128, 197);
    public static final Color COL_BTN_FONT = new Color(255, 255, 255);
    public static final Color COL_BTN_DISABLED = new Color(204, 226, 253);
    public static final Color COL_BORDER = new Color(152, 215, 232);
    public static final Color COL_TEXT_FONT_1 = new Color(3, 2, 108);
    public static final Color COL_TEXT_FONT_2 = new Color(18, 129, 248);
    // layout
    private final JPanel contentPanel = new JPanel();
    private OverallStatsViewer statsViewer;
    private InputWithLabel inputA;
    private InputWithLabel inputB;
    private InputWithLabel inputC;
    private InputWithLabel inputExperiments;

    private JButton btnStart;
    private JButton btnPause;
    private JButton btnCancel;
    private JButton btnSleepConfig;
    private JButton btnShiftConfig;

    private final SimController simController;
    private boolean simPaused;

    public FurnitureProdForm() {
//        ---- initialization of params of business logic
        this.simController = new SimController(this);
        this.simPaused = false;
//        ---- window: size, layout and behavior
        this.setSize(new Dimension(900,400));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(this.contentPanel);
        this.contentPanel.setBackground(COL_BG);
        this.contentPanel.setSize(this.getWidth(), this.getHeight());
//        ----- window: components
        this.createComponents();
//        ----- window: go live
        this.setVisible(true);
    }

    private String getStrDateTime(double timeMins) {
        int day = (int) timeMins / (60*24) + 1;
        timeMins -= (day-1) * 60*24;
        int hours = (int) Math.floor(timeMins/60.0);
        timeMins -= (hours) * 60;
        int min = (int)Math.ceil(timeMins);
        return String.format("Day-%d %02d:%02d", day, (min == 60 ? hours+1 : hours)%24, min%60);
    }

    @Override
    public void update(SimResults res) {
        if (res instanceof FurnitProdExpStats) {
            SwingUtilities.invokeLater(() -> {
                this.statsViewer.updateStatsList( ((FurnitProdExpStats)res).getResults() );
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Run")) {
            if (! this.simController.isSimRunning()) {
                this.simController.launchSimulation(this.inputA.getIntValue(), this.inputB.getIntValue(),
                        this.inputC.getIntValue(), this.inputExperiments.getIntValue());
                this.setBtnEnabled(this.btnStart, false);
                this.setBtnEnabled(this.btnPause, true);
                this.setBtnEnabled(this.btnCancel, true);
            }
        }
        else if (cmd.equals("Cancel")) {
            this.simController.terminateSimulation();
            this.setBtnEnabled(this.btnStart, true);
            this.setBtnEnabled(this.btnPause, false);
            this.setBtnEnabled(this.btnCancel, false);
            this.btnPause.setText("Pause");
            this.simPaused = false;
        }
        else if (cmd.equals("Pause")) {
            if (this.simPaused) {
                this.simController.resumeSimulation();
                this.btnPause.setText("Pause");
            } else {
                this.simController.pauseSimulation();
                this.btnPause.setText("Resume");
            }
            this.simPaused = !this.simPaused;
        }
    }

    private void createComponents() {
        this.btnStart = this.createBtn("Run");
        this.btnPause = this.createBtn("Pause");
        this.btnCancel = this.createBtn("Cancel");
        this.btnSleepConfig = this.createBtn("Config sleep");
        this.btnShiftConfig = this.createBtn("Config shift");
//        this.btnMaxSConfig = this.createBtn("Max-speed");
//        this.btnMaxSConfig = this.createBtn("Console logs");

        this.setBtnEnabled(btnPause, false);
        this.setBtnEnabled(btnCancel, false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(COL_BG);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(this.btnStart);
        buttonPanel.add(this.btnPause);
        buttonPanel.add(this.btnCancel);
        this.contentPanel.add(buttonPanel);

        JPanel conf1 = new JPanel();
        conf1.setBackground(COL_BG);
        JTextField txtSleep = this.createTextInput(6, "1000");
        this.btnSleepConfig.addActionListener(e -> {simController.setSleepTime(Integer.parseInt(txtSleep.getText()));});
        conf1.add(this.btnSleepConfig);
        conf1.add(txtSleep);

        JPanel conf2 = new JPanel();
        conf2.setBackground(COL_BG);
        JTextField txtShift = this.createTextInput(6, "5");
        this.btnShiftConfig.addActionListener(e -> {simController.setShiftTime(Integer.parseInt(txtShift.getText()));});
        conf2.add(this.btnShiftConfig);
        conf2.add(txtShift);

        JPanel conf3 = new JPanel();
        conf3.setBackground(COL_BG);
        JCheckBox checkLogs = new JCheckBox("Console-logs");
        checkLogs.setBackground(COL_BG);
        checkLogs.addActionListener(e -> {simController.setConsoleLogs(checkLogs.isSelected());});
        checkLogs.setSelected(false);
        conf3.add(checkLogs);
        JCheckBox checkMaxSpeed = new JCheckBox("Max-speed");
        checkMaxSpeed.setBackground(COL_BG);
        checkMaxSpeed.addActionListener(e -> {simController.setMaxSpeed(checkMaxSpeed.isSelected());});
        checkMaxSpeed.setSelected(false);
        conf3.add(checkMaxSpeed);

        JPanel configPanel = new JPanel();
        configPanel.setBackground(COL_BG);
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.add(conf1);
        configPanel.add(conf2);
        configPanel.add(conf3);
        this.contentPanel.add(configPanel);

        this.statsViewer = new OverallStatsViewer(700,200);
        this.contentPanel.add(this.statsViewer);

        JPanel carpentersInputsPanel = new JPanel();
        carpentersInputsPanel.setBackground(COL_BG);
        this.inputA = new InputWithLabel("Amount A:", 2, "2");
        this.inputB = new InputWithLabel("Amount B:", 2, "3");
        this.inputC = new InputWithLabel("Amount C:", 2, "20");
        this.inputExperiments = new InputWithLabel("Experiments:", 6, "10000");
        carpentersInputsPanel.add(this.inputA);
        carpentersInputsPanel.add(this.inputB);
        carpentersInputsPanel.add(this.inputC);
        carpentersInputsPanel.add(this.inputExperiments);
        this.contentPanel.add(carpentersInputsPanel);
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
}
