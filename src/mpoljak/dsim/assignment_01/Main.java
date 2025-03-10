package mpoljak.dsim.assignment_01;

import mpoljak.dsim.assignment_01.gui.SimVisualization;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimVisualization(new SimulationTask()); // executed as EDT task
            }
        });
    }

}
