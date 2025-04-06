package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

public class CarpenterResults {
    private int carpenterID;
    private int deskID;
    private int assignedOrderID = -1;
    private String group;
    private double orderBT = -1;
    private double orderET = -1;
    private boolean working = false;
    private String orderRepresentation;

    public CarpenterResults(int carpenterID, String group) {
        this.carpenterID = carpenterID;
        this.group = group;
    }

    public double getOrderBT() {
        return orderBT;
    }

    public void setOrderBT(double orderBT) {
        this.orderBT = orderBT;
    }

    public double getOrderET() {
        return orderET;
    }

    public void setOrderET(double orderET) {
        this.orderET = orderET;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getAssignedOrderID() {
        return assignedOrderID;
    }

    public void setAssignedOrderID(int assignedOrderID) {
        this.assignedOrderID = assignedOrderID;
    }

    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }
}
