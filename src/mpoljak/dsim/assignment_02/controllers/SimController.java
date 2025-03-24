package mpoljak.dsim.assignment_02.controllers;

import mpoljak.dsim.assignment_02.logic.EventSim;

/**
 * Controller is used for communication with business logic (some type of Simulation).
 */
public class SimController {
    private EventSim sim;
    private boolean simRunning; // true if it's stopped, also

    public SimController(EventSim simulation) {
        this.sim = simulation;
        this.simRunning = false;
    }

    public boolean isSimRunning() {
        return this.simRunning;
    }

    public void launchSimulation() {
        Runnable r = () -> {
            try {
                sim.simulate();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread t = new Thread(r, "Thread-Main");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
        this.simRunning = true;
    }

    public void terminateSimulation() {
        Runnable r = () -> sim.endSimulation();
        Thread t = new Thread(r, "Thread-Cancel");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
        this.simRunning = false;
    }

    public void pauseSimulation() {
        this.setSimPaused(true);
    }

    public void resumeSimulation() {
        this.setSimPaused(false);
    }

    private void setSimPaused(boolean paused) {
        Runnable r = new Runnable() {
            public void run() {
                sim.setPaused(paused);
            }
        };
        Thread t = new Thread(r, "Thread-"+(paused ? "Pause" : "Resume"));
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }
}
