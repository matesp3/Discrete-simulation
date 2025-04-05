package mpoljak.dsim.assignment_02.gui.components;

import javax.swing.*;

public class FurnitureProdAnim extends JPanel {
    private ResultViewer viewSimTime;

    public FurnitureProdAnim() {
        this.viewSimTime = new ResultViewer("Simulation Time");
        this.add(this.viewSimTime);
    }

    public void setSimTime(double minutes) {
         this.viewSimTime.setValue(minutes, 0);
    }
}
