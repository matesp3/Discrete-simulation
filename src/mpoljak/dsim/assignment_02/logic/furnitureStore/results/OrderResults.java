package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

public class OrderResults {
    private int orderID;
    private int deskID;
    private int assignedCarpenterID = -1;
    private String step;
    private double created;
    private double stepStart = -1; // of current step
    private double stepEnd = -1;   // of current step
    private String productType;


//    public OrderResults(long experimentNum, double simTime) {
//        super(experimentNum, simTime);
//    }

    public OrderResults(int orderID, int deskID, double created, String productType, String step) {
        this.orderID = orderID;
        this.deskID = deskID;
        this.created = created;
        this.step = step;
        this.productType = productType;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getDeskID() {
        return deskID;
    }

    public String getStep() {
        return step;
    }

    public double getCreated() {
        return created;
    }

    public String getProductType() {
        return productType;
    }

    public int getAssignedCarpenterID() {
        return assignedCarpenterID;
    }

    public void setAssignedCarpenterID(int assignedCarpenterID) {
        this.assignedCarpenterID = assignedCarpenterID;
    }

    public double getStepStart() {
        return stepStart;
    }

    public void setStepStart(double stepStart) {
        this.stepStart = stepStart;
    }

    public double getStepEnd() {
        return stepEnd;
    }

    public void setStepEnd(double stepEnd) {
        this.stepEnd = stepEnd;
    }
}
