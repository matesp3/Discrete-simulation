package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

public class FurnitureOrder {
    public enum FurnitureType {
        TABLE, CHAIR, WARDROBE
    }
    private final FurnitureType furnitureType;
    private final double timeOfCreation;
    private double timeOfCompletion;

    public FurnitureOrder(double timeOfCreation, FurnitureType furnitureType) {
        this.timeOfCreation = timeOfCreation;
        this.furnitureType = furnitureType;
    }

    public FurnitureType getFurnitureType() {
        return furnitureType;
    }

    public double getTimeOfCreation() {
        return this.timeOfCreation;
    }

    public double getTimeOfCompletion() {
        return this.timeOfCompletion;
    }

    public void setTimeOfCompletion(double timeOfCompletion) {
        this.timeOfCompletion = timeOfCompletion;
    }

    @Override
    public String toString() {
        return String.format("Order{type=%s; arisen->completed=%.02f -> %.2f}",
        this.furnitureType,this.timeOfCreation, this.timeOfCompletion);
    }

    public static void main(String[] args) {
        FurnitureOrder order = new FurnitureOrder(2.0, FurnitureType.CHAIR);
        order.setTimeOfCompletion(5.4894);
        System.out.println(order);
    }
}
