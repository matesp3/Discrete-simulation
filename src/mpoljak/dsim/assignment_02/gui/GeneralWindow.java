package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.contracts.ISimDelegate;
import mpoljak.dsim.assignment_02.controllers.SimController;
import mpoljak.dsim.assignment_02.logic.Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GeneralWindow extends javax.swing.JFrame implements ISimDelegate, ActionListener {

    // colors
    private final Color colBg = new Color(148, 172, 204);
    private final Color colBgContent = new Color(193, 193, 193);
    private final Color colBtn = new Color(81, 128, 197);
    private final Color colBtnFont = new Color(228, 230, 236);
    private final Color colBtnDisabled = new Color(204, 226, 253);
    private final Color colBorder = new Color(152, 215, 232);
    private final Color colTextFont = new Color(3, 2, 108);

    private JLabel lblVal;

    private SimController simController;
    private boolean simPaused;

    public GeneralWindow(Sim simulation) {
//        ---- initialization of params of business logic
        simulation.registerDelegate(this);
        this.simController = new SimController(simulation);
        this.simPaused = false;
//        ---- window: size, layout and behavior
        this.setSize(new Dimension(424,80));
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        ----- window: components
        this.createComponents();
//        ----- window: go live
        this.setVisible(true);
    }

    @Override
    public void refresh(int val) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                lblVal.setText(String.valueOf(val));
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Start")) {
            if (! this.simController.isSimRunning())
                this.simController.launchSimulation();
        }
        else if (cmd.equals("Cancel"))
                this.simController.terminateSimulation();
        else if (cmd.equals("Pause")) {
            if (this.simPaused) {
                this.simController.resumeSimulation();
                ((JButton) e.getSource()).setText("Pause");
            } else {
                this.simController.pauseSimulation();
                ((JButton) e.getSource()).setText("Resume");
            }
            this.simPaused = !this.simPaused;
        }
    }

    private void createComponents() {
        JLabel lblTitle = this.createLabel("Value: ");
        this.add(lblTitle);

        this.lblVal = this.createLabel("0");
        this.add(lblVal);

        JButton btnStart = this.createBtn("Start");
        this.add(btnStart);

        JButton btnPause = this.createBtn("Pause");
        this.add(btnPause);

        JButton btnCancel = this.createBtn("Cancel");
        this.add(btnCancel);
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
