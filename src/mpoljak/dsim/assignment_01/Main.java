package mpoljak.dsim.assignment_01;

import mpoljak.dsim.assignment_01.gui.SimVisualization;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimVisualization(); // executed as EDT task
            }
        });
    }

}
