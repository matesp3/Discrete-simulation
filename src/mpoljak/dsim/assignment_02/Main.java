package mpoljak.dsim.assignment_02;

import mpoljak.dsim.assignment_02.gui.GeneralWindow;
import mpoljak.dsim.assignment_02.logic.sim.EventSim;
import mpoljak.dsim.common.SimResults;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        EventSim simulation = new TestSim(10);
        SwingUtilities.invokeLater(() -> new GeneralWindow(simulation));
    }


    public static class TestSim extends EventSim {
        int val;
        public TestSim(long replicationsCount) {
            super(replicationsCount);
            val = 0;
        }

        @Override
        protected void experiment() throws InterruptedException {
            int cnt = 0;
            while (!this.isEnded() && cnt < 20) {
                this.checkPauseCondition();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                this.val = cnt;
                cnt++;
                this.notifyDelegates();
            }
        }

        @Override
        protected SimResults getLastResults() {
            return new SimResults(val);
        }
    }
}
