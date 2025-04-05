package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.controllers.SimController;
import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TimeSlider extends JPanel {
    private final int MINS_MIN = 0;  //0.25hrs
    private final int MINS_MID = 60; // 3hrs
    private final int MINS_MAX = 180; // 8hrs = 1 day

    private final JLabel resultScale;
    private JSlider sliderForSecs;

    public TimeSlider(SimController controller, boolean sliderHorizontal, int min, int max, int def, String displaySliderUnit,
                      String displayOtherUnit, double divisorOfSliderValue, Color bg) {
            if (controller == null)
                throw new NullPointerException();
        this.setLayout(new BoxLayout(this, sliderHorizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));

        this.resultScale = new JLabel();
        this.resultScale.setHorizontalAlignment(JLabel.CENTER);
        this.resultScale.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        this.sliderForSecs = new JSlider(sliderHorizontal ? JSlider.HORIZONTAL : JSlider.VERTICAL, min, max, def);
        controller.setShiftTime(sliderForSecs.getValue()/divisorOfSliderValue); // initialization
        this.sliderForSecs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    controller.setShiftTime(sliderForSecs.getValue()/divisorOfSliderValue);
                    resultScale.setText(formatOutput(displaySliderUnit, displayOtherUnit,
                            sliderForSecs.getValue()/divisorOfSliderValue));
                }
            }
        });
        this.sliderForSecs.setMajorTickSpacing((max)/4);
        this.sliderForSecs.setMinorTickSpacing((max)/12);
        this.sliderForSecs.setPaintTicks(true);
        this.sliderForSecs.setPaintLabels(true);
        this.resultScale.setText(formatOutput(displaySliderUnit, displayOtherUnit,
                this.sliderForSecs.getValue()/divisorOfSliderValue));

        this.add(this.sliderForSecs);
        this.add(Box.createRigidArea(sliderHorizontal ? new Dimension(20,0) : new Dimension(0,10)));
        this.add(this.resultScale);

        if (bg != null) {
            this.setBackground(bg);
            this.sliderForSecs.setBackground(bg);
        }
    }

    private static String formatOutput(String unit1, String unit2, double scaleValue) {
        return String.format("Sim-speed:  %.01f%s/1%s",  scaleValue, unit1, unit2);
    }
}