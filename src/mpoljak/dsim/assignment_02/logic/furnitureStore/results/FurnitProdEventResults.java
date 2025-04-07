package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class FurnitProdEventResults extends AfterEventResults {
    private final List<CarpenterResults> carpentersA;
    private final List<CarpenterResults> carpentersB;
    private final List<CarpenterResults> carpentersC;
    private final List<OrderResults> ordersA;
    private final List<OrderResults> ordersB;
    private final List<OrderResults> ordersCLow;
    private final List<OrderResults> ordersCHigh;

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

    /**
     *
     * @param cA amount of carpenters A
     * @param cB amount of carpenters B
     * @param cC amount of carpenters C
     */
    public FurnitProdEventResults(long experimentNum, double simTime, int cA, int cB, int cC) {
        super(experimentNum, simTime);
        this.carpentersA = new ArrayList<CarpenterResults>(cA);
        for (int i = 0; i < cA; i++)
            this.carpentersA.add(null);
        this.carpentersB = new ArrayList<CarpenterResults>(cB);
        for (int i = 0; i < cB; i++)
            this.carpentersB.add(null);
        this.carpentersC = new ArrayList<CarpenterResults>(cC);
        for (int i = 0; i < cC; i++)
            this.carpentersC.add(null);
        this.ordersA = new ArrayList<>();
        this.ordersB = new ArrayList<>();
        this.ordersCLow = new ArrayList<>();
        this.ordersCHigh = new ArrayList<>();
    }

    private CarpenterResults rawToModel(Carpenter raw) {
        CarpenterResults r = new CarpenterResults(raw.getCarpenterId(), raw.getGroup().toString());
        r.setDeskID(raw.getCurrentDeskID());
        r.setAssignedOrderID(raw.getCurrentOrder() == null ? -1 : raw.getCurrentOrder().getOrderID());
        r.setOrderBT(raw.getOrderProcessingBT());
        r.setOrderET(raw.getOrderProcessingET());
        r.setOrderRepresentation(raw.getCurrentOrder() == null ? "" : raw.getCurrentOrder().toString());
        r.setWorking(raw.isWorking());
        return r;
    }

    private OrderResults rawToModel(FurnitureOrder raw) {
        OrderResults r = new OrderResults(raw.getOrderID(), raw.getDeskID(), raw.getTimeOfOrderCreation(),
                raw.getProductType().toString(), raw.getNextTechStep().toString());
//        raw.setTechStepBegin(raw.get);
//        raw.setTechStepEnd();
        return r;
    }

    private void setNewCarpenters(Carpenter[] input, List<CarpenterResults> output) {
        for (int i = 0; i < input.length; i++) {
            output.set(i, this.rawToModel(input[i])); // size is the same for whole simulation
        }
    }

    private void setNewOrders(Queue<FurnitureOrder> input, List<OrderResults> output) {
        output.clear();
        for (FurnitureOrder o : input) {
            output.add(this.rawToModel(o)); // size of structure may vary each time
        }
    }

    public List<CarpenterResults> getCarpentersA() {
        return carpentersA;
    }

    public void setModelsCarpentersA(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersA);
    }

    public List<CarpenterResults> getCarpentersB() {
        return carpentersB;
    }

    public void setModelsCarpentersB(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersB);
    }

    public List<CarpenterResults> getCarpentersC() {
        return carpentersC;
    }

    public void setModelsCarpentersC(Carpenter[] carpenters) {
        this.setNewCarpenters(carpenters, this.carpentersC);
    }

    public List<OrderResults> getOrdersA() {
        return ordersA;
    }

    public void setOrdersA(Queue<FurnitureOrder> ordersA) {
        this.setNewOrders(ordersA, this.ordersA);
    }

    public List<OrderResults> getOrdersB() {
        return ordersB;
    }

    public void setOrdersB(Queue<FurnitureOrder> ordersB) {
        this.setNewOrders(ordersB, this.ordersB);
    }

    public List<OrderResults> getOrdersCLow() {
        return ordersCLow;
    }

    public void setOrdersCLow(Queue<FurnitureOrder> ordersCLow) {
        this.setNewOrders(ordersCLow, this.ordersCLow);
    }

    public List<OrderResults> getOrdersCHigh() {
        return ordersCHigh;
    }

    public void setOrdersCHigh(Queue<FurnitureOrder> orderCHigh) {
        this.setNewOrders(orderCHigh, this.ordersCHigh);
    }
}
