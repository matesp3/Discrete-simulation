package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import java.util.List;

public class FurnitProdEventResults extends AfterEventResults {
    private List<CarpenterResults> carpentersA;
    private List<CarpenterResults> carpentersB;
    private List<CarpenterResults> carpentersC;
    private List<OrderResults> ordersA;
    private List<OrderResults> ordersB;
    private List<OrderResults> ordersCLow;
    private List<OrderResults> orderCHigh;

    private StatResult wavgOrderQueueTimeA;
    private StatResult realOrderQueueTimeA;
    private StatResult wavgOrderQueueTimeB;
    private StatResult realOrderQueueTimeB;
    private StatResult wavgOrderQueueTimeStainingC;
    private StatResult realOrderQueueTimeStainingC;
    private StatResult wavgOrderQueueTimeFitInstC;
    private StatResult realOrderQueueTimeFitInstC;

    private StatResult wavgFreeCarpTimeA;
    private StatResult realFreeCarpTimeA;
    private StatResult wavgFreeCarpTimeB;
    private StatResult realFreeCarpTimeB;
    private StatResult wavgFreeCarpTimeC;
    private StatResult realFreeCarpTimeC;

    private StatResult avgOrderWaitAmountA;
    private StatResult realOrderWaitAmountA;
    private StatResult avgOrderWaitAmountB;
    private StatResult realOrderWaitAmountB;
    private StatResult avgOrderWaitAmountStainingC;
    private StatResult realOrderWaitAmountStainingC;
    private StatResult avgOrderWaitAmountFitInstC;
    private StatResult realOrderWaitAmountFitInstC;

    private StatResult avgFreeCarpAmountA;
    private StatResult realFreeCarpAmountA;
    private StatResult avgFreeCarpAmountB;
    private StatResult realFreeCarpAmountB;
    private StatResult avgFreeCarpAmountC;
    private StatResult realFreeCarpAmountC;

    private int assignedDesk;
    private int returnedDesk;

    public FurnitProdEventResults(long experimentNum, double simTime) {
        super(experimentNum, simTime);
    }

    public List<CarpenterResults> getCarpentersA() {
        return carpentersA;
    }

    public void setCarpentersA(List<CarpenterResults> carpentersA) {
        this.carpentersA = carpentersA;
    }

    public List<CarpenterResults> getCarpentersB() {
        return carpentersB;
    }

    public void setCarpentersB(List<CarpenterResults> carpentersB) {
        this.carpentersB = carpentersB;
    }

    public List<CarpenterResults> getCarpentersC() {
        return carpentersC;
    }

    public void setCarpentersC(List<CarpenterResults> carpentersC) {
        this.carpentersC = carpentersC;
    }

    public List<OrderResults> getOrdersA() {
        return ordersA;
    }

    public void setOrdersA(List<OrderResults> ordersA) {
        this.ordersA = ordersA;
    }

    public List<OrderResults> getOrdersB() {
        return ordersB;
    }

    public void setOrdersB(List<OrderResults> ordersB) {
        this.ordersB = ordersB;
    }

    public List<OrderResults> getOrdersCLow() {
        return ordersCLow;
    }

    public void setOrdersCLow(List<OrderResults> ordersCLow) {
        this.ordersCLow = ordersCLow;
    }

    public List<OrderResults> getOrderCHigh() {
        return orderCHigh;
    }

    public void setOrderCHigh(List<OrderResults> orderCHigh) {
        this.orderCHigh = orderCHigh;
    }
}
