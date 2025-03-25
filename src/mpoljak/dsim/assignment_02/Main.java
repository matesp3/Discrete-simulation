package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.common.SimCore;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SimCore simulation = new TestSim(1);
        SwingUtilities.invokeLater(() -> new GeneralWindow(simulation));
    }
    public static class TestSim extends SimCore {
        public TestSim(long replicationsCount) {
            super(replicationsCount);
        }

        @Override
        protected void experiment() {

        }
    }
}
