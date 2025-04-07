package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.controllers.FurnitProdSimController;
import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TimeSlider extends JPanel {

    private class SpeedMode {
        private String repr;
        private double simUnits;
        private long sleep;
        public SpeedMode(String repr, double simUnits, long sleep) {
            this.repr = repr;
            this.simUnits = simUnits;
            this.sleep = sleep;
        }
    }
    private SpeedMode[] modes;
    private final JLabel resultScale;
    private JSlider sliderForSecs;

    public TimeSlider(FurnitProdSimController controller, boolean sliderHorizontal,Color bg) {
            if (controller == null)
                throw new NullPointerException();
        this.setLayout(new BoxLayout(this, sliderHorizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        this.resultScale = new JLabel();
        this.resultScale.setHorizontalAlignment(JLabel.CENTER);
        this.resultScale.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        this.sliderForSecs = new JSlider(sliderHorizontal ? JSlider.HORIZONTAL : JSlider.VERTICAL, 0, 13, 5);
        this.initSpeedModes();
        controller.setSleepTime(this.modes[sliderForSecs.getValue()].sleep);
        controller.setShiftTime(this.modes[sliderForSecs.getValue()].simUnits); // initialization
        this.resultScale.setText(formatOutput(sliderForSecs.getValue()));

        this.sliderForSecs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    controller.setSleepTime(modes[sliderForSecs.getValue()].sleep);
                    controller.setShiftTime(modes[sliderForSecs.getValue()].simUnits);
                    resultScale.setText(formatOutput(sliderForSecs.getValue()));
                }
            }
        });
        this.sliderForSecs.setMinorTickSpacing(14);
        this.sliderForSecs.setPaintTicks(true);
        this.sliderForSecs.setPaintLabels(true);

        this.add(this.sliderForSecs);
        this.add(Box.createRigidArea(sliderHorizontal ? new Dimension(20,0) : new Dimension(0,10)));
        this.add(this.resultScale);

        if (bg != null) {
            this.setBackground(bg);
            this.sliderForSecs.setBackground(bg);
        }
    }

    private void initSpeedModes() {
        this.modes = new SpeedMode[14];
        this.modes[0] = new SpeedMode("1s", 1.0/60.0, 975); // 975 - 1.0/60.0
        this.modes[1] = new SpeedMode("3s", 1.5/60.0, 500); // 150
        this.modes[2] = new SpeedMode("5s", 1.2/60.0, 245); // 245
        this.modes[3] = new SpeedMode("15s", 4.5/60.0, 300); // 300
        this.modes[4] = new SpeedMode("30s", 0.09, 165); // 162 - 0.09
        this.modes[5] = new SpeedMode("1min", 0.1, 95); // 94 - 0.1
        this.modes[6] = new SpeedMode("3min", 50 / 60.0, 250); // 250
        this.modes[7] = new SpeedMode("5min", 1.25, 250);
        this.modes[8] = new SpeedMode("15min", 3.85, 250);
        this.modes[9] = new SpeedMode("30min", 7.25, 250);
        this.modes[10] = new SpeedMode("1h", 15, 250);
        this.modes[11] = new SpeedMode("4h", 50, 250);
        this.modes[12] = new SpeedMode("8h", 100, 250);
        this.modes[13] = new SpeedMode("1week", 800, 250);
    }

    private String formatOutput(int mode) {
        return String.format("Sim-speed:  %s/1s",  this.modes[mode].repr);
    }
}