package mpoljak.dsim.assignment_02.controllers;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

import javax.swing.*;

/**
 * Controller is used for communication with business logic (some type of Simulation).
 */
public class SimController {
    private final FurnitureProdForm gui;
    private FurnitureProductionSim sim;
    private boolean simRunning; // true if it's stopped, also

    public SimController(FurnitureProdForm gui) {
        this.gui = gui;
        this.sim = null;
        this.simRunning = false;
    }

    public boolean isSimRunning() {
        return this.simRunning;
    }

    public void launchSimulation(int groupA, int groupB, int groupC, int experiments) {
        Runnable r = () -> {
            try {
                this.sim = new FurnitureProductionSim(experiments, groupA, groupB, groupC);
                this.sim.registerDelegate(this.gui);
                this.sim.simulate();
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
        Runnable r = () -> sim.setPaused(paused);
        Thread t = new Thread(r, "Thread-"+(paused ? "Pause" : "Resume"));
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setSleepTime(long millis) {
        Runnable r = () -> sim.setSleepTime(millis);
        Thread t = new Thread(r, "Thread-config sleepTime");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setShiftTime(double time) {
        Runnable r = () -> sim.setShiftTime(time);
        Thread t = new Thread(r, "Thread-config shiftTime");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setMaxSpeed(boolean enable) {
        Runnable r = () -> sim.enableMaxSimSpeed(enable);
        Thread t = new Thread(r, "Thread-config maxSpeed");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setConsoleLogs(boolean enable) {
        Runnable r = () -> sim.setDebugMode(enable);
        Thread t = new Thread(r, "Thread-config consoleLogs");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }
}
