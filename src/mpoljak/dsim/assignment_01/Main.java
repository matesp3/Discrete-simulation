package mpoljak.dsim.assignment_01;

import mpoljak.dsim.assignment_01.controllers.SimController;
import mpoljak.dsim.assignment_01.gui.SimVisualization;
import mpoljak.dsim.assignment_01.logic.experiments.SingleSupply;
import mpoljak.dsim.assignment_01.logic.experiments.Supplier;
import mpoljak.dsim.assignment_01.logic.experiments.SupplyStrategy;
import mpoljak.dsim.assignment_01.logic.generators.ContinuosUniformRnd;
import mpoljak.dsim.assignment_01.logic.tasks.SimulationTask;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimVisualization(new SimController()); // executed as EDT task
            }
        });
    }

}
