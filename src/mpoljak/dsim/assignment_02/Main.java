package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.EventSim;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EventSim simulation = new TestSim(1);
        SwingUtilities.invokeLater(() -> {
            new GeneralWindow(simulation);
        });
    }
    public static class TestSim extends EventSim {
        public TestSim(long replicationsCount) {
            super(replicationsCount);
        }

        @Override
        protected void experiment() {

        }
    }
}
