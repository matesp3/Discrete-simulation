package mpoljak.dsim.assignment_01;

import mpoljak.dsim.assignment_01.gui.SimVisualization;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // executed as EDT task
        SwingUtilities.invokeLater(SimVisualization::new);
    }

}
