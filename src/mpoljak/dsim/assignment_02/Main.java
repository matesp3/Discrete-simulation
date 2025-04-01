package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;
import mpoljak.dsim.assignment_02.logic.ticketSelling.sim.TicketSellingSim;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventSim simulation = new FurnitureStoreSim(3, 3,3,3);
        simulation.setShiftTime(Double.MAX_VALUE);
        simulation.setSleepTime(0);
        simulation.simulate();
//        SwingUtilities.invokeLater(() -> new GeneralWindow(simulation));
    }
}
