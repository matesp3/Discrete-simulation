package mpoljak.dsim.assignment_02.controllers;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;
import mpoljak.dsim.utils.DoubleComp;

/**
 * Controller is used for communication with business logic (some type of Simulation).
 */
public class FurnitProdSimController {
    private final FurnitureProdForm gui;
    private FurnitureProductionSim sim;
    private boolean simRunning; // true if it's stopped, also
    private boolean maxSpeedOn;
    private boolean consoleLogsOn;
    private double shiftTime; // seconds
    private long sleepTime; // milliseconds

    public FurnitProdSimController(FurnitureProdForm gui) {
        this.gui = gui;
        this.sim = null;
        this.simRunning = false;
        this.maxSpeedOn = true;
        this.consoleLogsOn = false;
        this.shiftTime = 5*60;     // secs
        this.sleepTime = 250;   // millis
    }

    public boolean isSimRunning() {
        return this.simRunning;
    }

    public void launchSimulation(int groupA, int groupB, int groupC, int experiments, double simulatedDays) {
        Runnable r = () -> {
            try {
                this.sim = new FurnitureProductionSim(experiments, groupA, groupB, groupC, simulatedDays*8*3600);// 3600s*8hod*sim_dni [secs]
                this.sim.registerDelegate(this.gui);
                if (this.maxSpeedOn) {
                    this.sim.setEnabledMaxSimSpeed(true);
                }
                else {
                    this.sim.setEnabledMaxSimSpeed(false);
                    this.sim.setShiftTime(this.shiftTime);
                    this.sim.setSleepTime(this.sleepTime);
                }
                this.sim.setDebugMode(this.consoleLogsOn);
                this.sim.simulate();
                this.simRunning = false;
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
        if (this.sim == null || !this.simRunning)
            return;
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
        if (this.sim == null || !this.simRunning)
            return;
        Runnable r = () -> sim.setPaused(paused);
        Thread t = new Thread(r, "Thread-"+(paused ? "Pause" : "Resume"));
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setSleepTime(long millis) {
        this.sleepTime = millis < 0 ? 0 : millis;
        if (this.sim == null)
            return;
        Runnable r = () -> sim.setSleepTime(millis);
        Thread t = new Thread(r, "Thread-config sleepTime");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setShiftTime(double time) {
        this.shiftTime = DoubleComp.compare(time, 0) == -1 ? 0 : time;
        if (this.sim == null)
            return;
        Runnable r = () -> sim.setShiftTime(time);
        Thread t = new Thread(r, "Thread-config shiftTime");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setShiftAndSleepTime(double shiftTime, long sleepTime) {
        this.shiftTime = DoubleComp.compare(shiftTime, 0) == -1 ? 0 : shiftTime;
        if (this.sim == null)
            return;
        Runnable r = () -> sim.setShiftAndSleepTime(shiftTime, sleepTime);
        Thread t = new Thread(r, "Thread-config shiftTime");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setEnabledMaxSpeed(boolean enabled) {
        this.maxSpeedOn = enabled;
        if (this.sim == null)
            return;
        Runnable r = () -> sim.setEnabledMaxSimSpeed(enabled);
        Thread t = new Thread(r, "Thread-config maxSpeed");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }

    public void setEnabledConsoleLogs(boolean enabled) {
        this.consoleLogsOn = enabled;
        if (this.sim == null)
            return;
        Runnable r = () -> sim.setDebugMode(enabled);
        Thread t = new Thread(r, "Thread-config consoleLogs");
        t.setDaemon(true); // if GUI ends, simulation also
        t.start();
    }
}
