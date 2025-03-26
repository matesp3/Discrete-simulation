package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.gui.components.ResultViewer;
import mpoljak.dsim.assignment_02.logic.sim.TicketSelling;
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

    private SimController simController;
    private boolean simPaused;
    private JButton btnStart;
    private JButton btnPause;
    private JButton btnCancel;
    private ResultViewer repInfo;
    private ResultViewer timeInfo;
    private ResultViewer busyInfo;
    private ResultViewer queueInfo;
    private ResultViewer eventInfo;

    public GeneralWindow(SimCore simulation) {
//        ---- initialization of params of business logic
        simulation.registerDelegate(this);
        this.simController = new SimController(simulation);
        this.simPaused = false;
//        ---- window: size, layout and behavior
        this.setSize(new Dimension(600,400));
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
        TicketSelling.TicketSellRes r = (TicketSelling.TicketSellRes) res;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repInfo.setValue(r.getReplication());
                busyInfo.setValue(r.isWorkerBusy());
                queueInfo.setValue(r.getQueueLength());
                timeInfo.setValue(r.getTime(), -1);
                eventInfo.setValue(r.getEventId());
            }
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
        this.repInfo = new ResultViewer("Replication");
        this.timeInfo = new ResultViewer("Time");
        this.busyInfo = new ResultViewer("Worker busy");
        this.queueInfo = new ResultViewer("Queue length");
        this.eventInfo = new ResultViewer("Event");

        this.btnStart = this.createBtn("Start");
        this.btnPause = this.createBtn("Pause");
        this.btnCancel = this.createBtn("Cancel");

        this.setBtnEnabled(btnPause, false);
        this.setBtnEnabled(btnCancel, false);

        this.add(this.repInfo);
        this.add(this.timeInfo);
        this.add(this.busyInfo);
        this.add(this.queueInfo);
        this.add(this.eventInfo);
        this.add(this.btnStart);
        this.add(this.btnPause);
        this.add(this.btnCancel);
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

    private JTextField createTextInput(int expectedLettersCount, String hintText) {
        JTextField txtField = new JTextField(hintText);
//        txtField.setSize(new Dimension(width, TXT_FIELD_HEIGHT));
        txtField.setColumns( (int)((5.0/7.0)*expectedLettersCount)+1 );
        return txtField;
    }
}
