package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import java.util.Comparator;

public class Carpenter {
    public enum GROUP {
        A,B,C
    }

    public static final int IN_STORAGE = -1;
    private final GROUP group;
    private final int carpenterId;
    private double lastWorkStart;
    private double lastWorkEnd;
    private int deskID;
    private boolean working;

    public Carpenter(GROUP group, int carpenterID) {
        this.group = group;
        this.carpenterId = carpenterID;
        this.working = false;
        this.deskID = IN_STORAGE;
        this.lastWorkStart = -1;
        this.lastWorkEnd = -1;
    }

    /**
     * @return group ID, in which is carpenter working.
     */
    public GROUP getGroup() {
        return this.group;
    }

    public int getCarpenterId() {
        return this.carpenterId;
    }

    /**
     * @return amount of time of working on lastly processed order
     * @throws RuntimeException if carpenter is working right now or was not working at all
     */
    public double getLastWorkDuration() throws RuntimeException {
        if (this.working || this.deskID == IN_STORAGE)
            throw new RuntimeException("Carpenter is working or is still in storage");
        return this.lastWorkEnd - this.lastWorkStart;
    }

    public double getLastWorkStart() {
        return this.lastWorkStart;
    }

    /**
     * @param lastWorkStart
     * @throws RuntimeException if Carpenter is already working
     */
    public void setLastWorkStart(double lastWorkStart) {
        if (this.working)
            throw new RuntimeException("Carpenter is already working");
        this.lastWorkEnd = -1;
        this.lastWorkStart = lastWorkStart;
        this.working = true;
    }

    public double getLastWorkEnd() {
        return this.lastWorkEnd;
    }

    public void setLastWorkEnd(double lastWorkEnd) {
        this.lastWorkEnd = lastWorkEnd;
        this.working = false;
    }

    /**
     * @return ID of desk where carpenter is standing right now or <code>IN_STORAGE</code> value if he is in wood
     * storage.
     */
    public int getDeskID() {
        return this.deskID;
    }

    /**
     * @param deskID
     * @throws RuntimeException if Carpenter is already working
     */
    public void setDeskID(int deskID) {
        if (this.working)
            throw new RuntimeException("Carpenter is already working");
        this.deskID = deskID;
    }

    /**
     * @return <code>true</code> if he has started some work
     */
    public boolean isWorking() {
        return this.working;
    }

    @Override
    public String toString() {
        return String.format("Carp{working=%b; ID=%d}", this.working, this.carpenterId);
    }

    public static void main(String[] args) {
        Carpenter carpenter = new Carpenter(GROUP.A, 1);
        System.out.println(carpenter.getGroup());
        System.out.println(carpenter.isWorking());
//        System.out.println(carpenter.getLastWorkDuration());
        System.out.println(carpenter.getLastWorkStart());
        carpenter.setDeskID(1);
        carpenter.setLastWorkStart(5.0);
        System.out.println(carpenter.getLastWorkStart());
        System.out.println(carpenter.isWorking());
        carpenter.setLastWorkEnd(56.4);
        System.out.println(carpenter.getLastWorkEnd());
        System.out.println(carpenter.isWorking());
        System.out.println(carpenter.getLastWorkDuration());
        carpenter.setLastWorkStart(60.0);
        System.out.println(carpenter.getLastWorkStart());
        System.out.println(carpenter.isWorking());
//        System.out.println(carpenter.getLastWorkEnd());
//        System.out.println(carpenter.getLastWorkDuration());

//        System.out.println("compare(false, true): "+Boolean.compare(false, true));
//        System.out.println("compare(true, true): "+Boolean.compare(true, true));
//        System.out.println("compare(true, false): "+Boolean.compare(true, false));
//        System.out.println("compare(false, false): "+Boolean.compare(false, false));
    }
}
