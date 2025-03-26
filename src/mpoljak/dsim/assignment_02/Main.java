package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.sim.EventSim;
import mpoljak.dsim.assignment_02.logic.sim.TicketSelling;
import mpoljak.dsim.common.SimResults;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EventSim simulation = new TicketSelling(10,2);
        SwingUtilities.invokeLater(() -> new GeneralWindow(simulation));
    }
}
