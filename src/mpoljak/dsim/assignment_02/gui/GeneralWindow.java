package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.gui.components.ResultViewer;
import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSelling;
import mpoljak.dsim.common.ISimDelegate;
import mpoljak.dsim.assignment_02.controllers.SimController;
import mpoljak.dsim.common.SimCore;
import mpoljak.dsim.common.SimResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralWindow extends javax.swing.JFrame implements ISimDelegate, ActionListener {

    // colors
    public static final Color colBg = new Color(148, 172, 204);
    public static final Color colBgContent = new Color(193, 193, 193);
    public static final Color colBtn = new Color(81, 128, 197);
    public static final Color colBtnFont = new Color(255, 255, 255);
    public static final Color colBtnDisabled = new Color(204, 226, 253);
    public static final Color colBorder = new Color(152, 215, 232);
    public static final Color colTextFont = new Color(3, 2, 108);
    public static final Color colTextFont2 = new Color(18, 129, 248);

    private final SimController simController;
    private boolean simPaused;
    private JButton btnStart;
    private JButton btnPause;
    private JButton btnCancel;
    private ResultViewer repInfo;
    private ResultViewer dateTimeInfo;
    private ResultViewer busyInfo;
    private ResultViewer queueInfo;
    private ResultViewer eventInfo;

    public GeneralWindow(SimCore simulation) {
//        ---- initialization of params of business logic
        simulation.registerDelegate(this);
        this.simController = new SimController(simulation);
        this.simPaused = false;
//        ---- window: size, layout and behavior
        this.setSize(new Dimension(400,300));
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(this.colBg);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ----- window: components
        this.createComponents();
//        ----- window: go live
        this.setVisible(true);
    }

    @Override
    public void update(SimResults res) {
        /*
         todo: 0. implement simCore.receiveUpdates(<? extends SimResults> results)
         todo: 1.typecast:find out class type => 2. call proper method based on component => 3. find proper component from list by ID or name => 4. update component
         */
        TicketSelling.TicketSellRes r = (TicketSelling.TicketSellRes) res;
        SwingUtilities.invokeLater(() -> {
            repInfo.setValue(r.getReplication());
            busyInfo.setValue(r.isWorkerBusy());
            queueInfo.setValue(r.getQueueLength());
            double mins = r.getTime();
            int day = (int) mins / (60*24) + 1;
            mins -= (day-1) * 60*24;
            int hours = (int) Math.floor(mins/60.0);
            mins -= (hours) * 60;
            int min = (int)Math.ceil(mins);
            dateTimeInfo.setValue(String.format("Day-%d %02d:%02d", day, (min == 60 ? hours+1 : hours)%24, min%60));
            eventInfo.setValue(r.getEventId());
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Start")) {
            if (! this.simController.isSimRunning()) {
                this.simController.launchSimulation();
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
        this.btnStart = this.createBtn("Start");
        this.btnPause = this.createBtn("Pause");
        this.btnCancel = this.createBtn("Cancel");

        this.setBtnEnabled(btnPause, false);
        this.setBtnEnabled(btnCancel, false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(this.btnStart);
        buttonPanel.add(this.btnPause);
        buttonPanel.add(this.btnCancel);

        this.repInfo = new ResultViewer("Replication");
        this.dateTimeInfo = new ResultViewer("DateTime");
        this.busyInfo = new ResultViewer("Worker busy");
        this.queueInfo = new ResultViewer("Queue length");
        this.eventInfo = new ResultViewer("Event");

        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));

        infoBox.add(buttonPanel);
        infoBox.add(this.repInfo);
        infoBox.add(this.dateTimeInfo);
        infoBox.add(this.busyInfo);
        infoBox.add(this.queueInfo);
        infoBox.add(this.eventInfo);
        this.add(infoBox);

    }

    private JButton createBtn(String caption) {
        JButton btn = new JButton(caption);
        btn.setBackground(colBtn);
        btn.setActionCommand(caption);
        btn.addActionListener(this);
        btn.setForeground(colBtnFont);
//        btn.setBorder(BorderFactory.createLineBorder(this.colBorder));
//        btn.setSize(50, 30);
        return btn;
    }

    private void setBtnEnabled(JButton btn, boolean enabled) {
        btn.setBackground(enabled ? colBtn : colBtnDisabled);
        btn.setEnabled(enabled);
    }

    private JLabel createLabel(String caption) {
        JLabel label = new JLabel(caption);
        label.setForeground(colTextFont);
        return label;
    }

    private JTextField createTextInput(int expectedLettersCount, String hintText) {
        JTextField txtField = new JTextField(hintText);
//        txtField.setSize(new Dimension(width, TXT_FIELD_HEIGHT));
        txtField.setColumns( (int)((5.0/7.0)*expectedLettersCount)+1 );
        return txtField;
    }
}
