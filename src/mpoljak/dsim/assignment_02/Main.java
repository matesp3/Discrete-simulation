package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureStoreSim;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        FurnitureStoreSim simulation = new FurnitureStoreSim(10_000, 2, 2, 18);
//        FurnitureStoreSim simulation = new FurnitureStoreSim(10_000, 3, 3, 20);
//        FurnitureStoreSim simulation = new FurnitureStoreSim(10_000, 1, 2, 16);
        simulation.setShiftTime(Double.MAX_VALUE);
        simulation.setSleepTime(0);
        simulation.simulate();
        System.out.println(simulation.getCurrentReplication());
//        SwingUtilities.invokeLater(() -> new GeneralWindow(simulation));
    }
}
