package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import java.util.ArrayList;
import java.util.Arrays;

public class DeskAllocation {
    private final ArrayList<FurnitureOrder> desks;
    private int firstFree;

    public DeskAllocation(int amountOfDesks) {
        this.desks = new ArrayList<>(amountOfDesks*2);
        for (int i = 0; i < amountOfDesks*2; i++) {
            this.desks.add(null);
        }
        this.firstFree = 0;
    }

    public void setDeskFree(int deskId, FurnitureOrder userIdentity) {
        if (deskId < 0 || deskId >= this.desks.size())
            throw new IllegalArgumentException("Desk ID " + deskId + " does not exist");
        if (this.desks.get(deskId) != userIdentity)
            throw new IllegalArgumentException("Violation of desk freeing. This identity cannot free desk that doesn't"
                    + " belong to him ");
        this.desks.set(deskId, null);
        if (this.firstFree == -1 || deskId < this.firstFree)
            this.firstFree = deskId;
    }

    /**
     * @return <code>-1</code> if there was no free desk to occupy or <code>applicantIdentity</code> not provided,
     * else <code>ID</code> of assigned desk (strategy of assigning is an internal logic).
     */
    public int occupyDesk(FurnitureOrder applicantIdentity) {
        if (applicantIdentity == null)
            throw new NullPointerException("applicantIdentity is null");
        if (this.firstFree == -1) {
            this.desks.add(applicantIdentity);
            return this.desks.size() - 1;
        }

        int assigned = this.firstFree;
        this.desks.set(assigned, applicantIdentity);

        this.firstFree = -1;
        for (int i = assigned+1; i < this.desks.size(); i++) {
            if (this.desks.get(i) == null) {
                this.firstFree = i;
                break;
            }
        }
        return assigned;
    }

    public void freeAllDesks() {
        this.desks.replaceAll(ignored -> null);
        this.firstFree = 0;
    }

    @Override
    public String toString() {
        return "DeskAllocation{" +
                "firstFree=" + firstFree +
                ", desks=" + this.desks +
                '}';
    }

    public static void main(String[] args) {
        DeskAllocation manager = new DeskAllocation(5);
        manager.occupyDesk(new FurnitureOrder(1, 15, FurnitureOrder.Product.CHAIR));
        FurnitureOrder o = new FurnitureOrder(2, 9, FurnitureOrder.Product.TABLE);
        int deskID = manager.occupyDesk(o);
        manager.occupyDesk(new FurnitureOrder(3, 2, FurnitureOrder.Product.WARDROBE));
        System.out.println(manager);
        System.out.println("Removing "+o+" from desk["+deskID+"]");
        manager.setDeskFree(deskID, o);
        System.out.println(manager);
        System.out.println("Freeing everything...");
        manager.freeAllDesks();
        System.out.println(manager);
        // ok
    }
}
