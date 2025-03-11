package mpoljak.dsim.assignment_01.gui;

import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimVisualization extends JFrame implements ActionListener {
    private static final int CANVAS_WIDTH = 1400;
    private static final int CANVAS_HEIGHT = 840;
    private final JButton btnStart;
    private final JButton btnStop;
    private final GridBagConstraints constraints;
    private final Color col1 = new Color(43, 201, 201);
    private final Color col2 = new Color(66, 227, 54);

    private final SimulationTask simTask;

    public SimVisualization(SimulationTask simTask) {
        this.simTask = simTask;
        //      ---- app icon
//    ImageIcon icon = new ImageIcon(System.getProperty("user.dir")+"/GeoApp_imgs/GeoApp-icon.png");
//    this.setIconImage(icon.getImage());
//      ---- frame properties
        this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.setLayout(new BorderLayout());
//        this.setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
//        this.getContentPane().setLayout(new GridBagLayout());
        this.constraints = new GridBagConstraints();
        this.constraints.insets = new Insets(3, 10, 3, 10);
        this.setResizable(false); // if I don't want client to resize window
        this.setTitle("MC-simulation of car components costs");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
//      ---- main panel
        JPanel mainPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        mainPanel.setLayout(gridBagLayout);
        this.add(mainPanel, BorderLayout.CENTER);
//      ---- colors
        Color frameColor = new Color(5, 4, 56);
        this.getContentPane().setBackground(frameColor);
//      ---- components
        this.btnStart = createBtn("Start");
        this.btnStop = createBtn("Stop");

//        this.btnStop.setEnabled(false);
        mainPanel.add(btnStart, constraints);
        mainPanel.add(btnStop, constraints);
        JPanel rectangle = new JPanel();
        rectangle.setSize(100, 50);
        rectangle.setBackground(col1);
        JButton btn = new JButton("Toggler");
        btn.addActionListener(e->{
            if (rectangle.getBackground() == col1)
                rectangle.setBackground(col2);
            else
                rectangle.setBackground(col1);
        });
        mainPanel.add(btn, constraints);
        mainPanel.add(rectangle, constraints);
//      ---- set all visible
        this.pack(); // probably used when dimensions are not set??
        this.setVisible(true);
//        SwingUtilities.invokeLater(new Runnable() {})
    }

    private JButton createBtn(String btnName) {
        JButton btn = new JButton(btnName);
        Color btnColor = new Color(210, 94, 94);
        btn.setBackground(btnColor);
//        btn.addActionListener(e-> System.out.println("clicked"));
        btn.setActionCommand(btnName);
        btn.addActionListener(this);
        btn.setForeground(Color.BLACK);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            this.btnStop.setEnabled(true);
            this.btnStart.setEnabled(false);
            this.simTask.execute();
        }
        else if (e.getActionCommand().equals("Stop")) {
            this.simTask.cancel(true);
        }

    }
}
