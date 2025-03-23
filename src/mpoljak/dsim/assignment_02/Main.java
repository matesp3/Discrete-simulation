package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.Sim;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Sim simulation = new Sim();
        SwingUtilities.invokeLater(() -> {
            new GeneralWindow(simulation);
        });
    }
}
