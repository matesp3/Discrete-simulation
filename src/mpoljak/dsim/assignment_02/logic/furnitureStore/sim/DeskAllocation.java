package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import java.util.Arrays;

public class DeskAllocation {
    private final String[] desks;
    private int firstFree;

    public DeskAllocation(int amountOfDesks) {
        this.desks = new String[amountOfDesks];
        this.firstFree = 0;
    }

    public boolean isAnyDeskAvailable() {
        return this.firstFree > -1;
    }

    public void setDeskFree(int deskId, String userIdentity) {
        if (deskId < 0 || deskId >= this.desks.length)
            throw new IllegalArgumentException("Desk ID " + deskId + " does not exist");
        if (this.desks[deskId] == null)
            throw new RuntimeException("Desk with ID="+deskId+" is already empty.");
        if (!this.desks[deskId].equalsIgnoreCase(userIdentity))
            throw new IllegalArgumentException("Violation of desk freeing. Identity of applicant is different from "
                    + "desk's current user");
        this.desks[deskId] = null;
        if (this.firstFree == -1 || deskId < this.firstFree)
            this.firstFree = deskId;
    }

    /**
     * @return <code>-1</code> if there was no free desk to occupy or <code>applicantIdentity</code> not provided,
     * else <code>ID</code> of assigned desk (strategy of assigning is an internal logic).
     */
    public int occupyDesk(String applicantIdentity) {
        if (applicantIdentity == null || applicantIdentity.isBlank() || this.firstFree == -1)
            return -1;
        int assigned = this.firstFree;
        this.desks[assigned] = applicantIdentity;

        this.firstFree = -1;
        for (int i = assigned; i < this.desks.length; i++) {
            if (this.desks[i] == null) {
                this.firstFree = i;
                break;
            }
        }
        return assigned;
    }

    public void freeAllDesks() {
        Arrays.fill(this.desks, null);
        this.firstFree = 0;
    }

    @Override
    public String toString() {
        return "DeskAllocation{" +
                "firstFree=" + firstFree +
                ", desks=" + Arrays.toString(desks) +
                '}';
    }
}
