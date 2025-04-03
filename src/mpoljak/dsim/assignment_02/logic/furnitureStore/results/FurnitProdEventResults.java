package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import java.util.List;

public class FurnitProdEventResults extends AfterEventResults {
    private List<CarpenterResults> changedCarpenters;
    private List<OrderResults> changedOrders;

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

    public List<CarpenterResults> getChangedCarpenters() {
        return changedCarpenters;
    }

    public void setChangedCarpenters(List<CarpenterResults> changedCarpenters) {
        this.changedCarpenters = changedCarpenters;
    }

    public List<OrderResults> getChangedOrders() {
        return changedOrders;
    }

    public void setChangedOrders(List<OrderResults> changedOrders) {
        this.changedOrders = changedOrders;
    }
}
