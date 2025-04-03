package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

import javax.swing.*;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FurnitureProdForm form = new FurnitureProdForm();
            }
        });

    }
}
