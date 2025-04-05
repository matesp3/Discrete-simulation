package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.TicketsSellingWindow;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        FurnitureProductionSim simulation = new FurnitureProductionSim(10_000, 2, 2, 18,249*8*60); // 60min*8hod*249dni = 358_560 [min]
//        FurnitureStoreSim simulation = new FurnitureStoreSim(10_000, 3, 3, 20);
//        FurnitureStoreSim simulation = new FurnitureStoreSim(10_000, 1, 2, 16);

        simulation.setShiftTime(100); // min
        simulation.setSleepTime(500);
//        simulation.simulate();
//        System.out.println(simulation.getCurrentReplication());
        SwingUtilities.invokeLater(() -> new TicketsSellingWindow(simulation));

//        Generator rnd = new ContinuosUniformRnd(0, 10000);
//        Queue<DiscreteEvent> ids = new PriorityQueue<>(15, new DiscreteEvent.EventComparator());
//        for (int i = 0; i < 1000; i++) {
//            ids.add(new AssemblingBeginning(rnd.sample(), null, null));
//        }
//        for (int i = 0; i < ids.size(); i++) {
//            System.out.println(ids.poll().getExecutionTime());
//        }
    }
}
