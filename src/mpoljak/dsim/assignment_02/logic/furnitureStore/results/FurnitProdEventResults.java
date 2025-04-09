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

    private StatResult.Simple ordersWaitingQueueCount;
    private StatResult.Simple ordersAssemblingQueueCount;
    private StatResult.Simple ordersStainingQueueCount;
    private StatResult.Simple ordersFitInstQueueCount;

    private StatResult.Simple ordersWaitingQueueTime;
    private StatResult.Simple ordersAssemblingQueueTime;
    private StatResult.Simple ordersStainingQueueTime;
    private StatResult.Simple ordersFitInstQueueTime;

    private StatResult.Simple utilizationGroupA;
    private StatResult.Simple utilizationGroupB;
    private StatResult.Simple utilizationGroupC;

    private StatResult.Simple orderTimeInSystem;
    private StatResult.Simple allocatedDesksCount;

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

    public StatResult.Simple getOrdersWaitingQueueCount() {
        return ordersWaitingQueueCount;
    }

    public void setOrdersWaitingQueueCount(StatResult.Simple ordersWaitingQueueCount) {
        this.ordersWaitingQueueCount = ordersWaitingQueueCount;
    }

    public StatResult.Simple getOrdersAssemblingQueueCount() {
        return ordersAssemblingQueueCount;
    }

    public void setOrdersAssemblingQueueCount(StatResult.Simple ordersAssemblingQueueCount) {
        this.ordersAssemblingQueueCount = ordersAssemblingQueueCount;
    }

    public StatResult.Simple getOrdersStainingQueueCount() {
        return ordersStainingQueueCount;
    }

    public void setOrdersStainingQueueCount(StatResult.Simple ordersStainingQueueCount) {
        this.ordersStainingQueueCount = ordersStainingQueueCount;
    }

    public StatResult.Simple getOrdersFitInstQueueCount() {
        return ordersFitInstQueueCount;
    }

    public void setOrdersFitInstQueueCount(StatResult.Simple ordersFitInstQueueCount) {
        this.ordersFitInstQueueCount = ordersFitInstQueueCount;
    }

    public StatResult.Simple getOrdersWaitingQueueTime() {
        return ordersWaitingQueueTime;
    }

    public void setOrdersWaitingQueueTime(StatResult.Simple ordersWaitingQueueTime) {
        this.ordersWaitingQueueTime = ordersWaitingQueueTime;
    }

    public StatResult.Simple getOrdersAssemblingQueueTime() {
        return ordersAssemblingQueueTime;
    }

    public void setOrdersAssemblingQueueTime(StatResult.Simple ordersAssemblingQueueTime) {
        this.ordersAssemblingQueueTime = ordersAssemblingQueueTime;
    }

    public StatResult.Simple getOrdersStainingQueueTime() {
        return ordersStainingQueueTime;
    }

    public void setOrdersStainingQueueTime(StatResult.Simple ordersStainingQueueTime) {
        this.ordersStainingQueueTime = ordersStainingQueueTime;
    }

    public StatResult.Simple getOrdersFitInstQueueTime() {
        return ordersFitInstQueueTime;
    }

    public void setOrdersFitInstQueueTime(StatResult.Simple ordersFitInstQueueTime) {
        this.ordersFitInstQueueTime = ordersFitInstQueueTime;
    }

    public StatResult.Simple getUtilizationGroupA() {
        return utilizationGroupA;
    }

    public void setUtilizationGroupA(StatResult.Simple utilizationGroupA) {
        this.utilizationGroupA = utilizationGroupA;
    }

    public StatResult.Simple getUtilizationGroupB() {
        return utilizationGroupB;
    }

    public void setUtilizationGroupB(StatResult.Simple utilizationGroupB) {
        this.utilizationGroupB = utilizationGroupB;
    }

    public StatResult.Simple getUtilizationGroupC() {
        return utilizationGroupC;
    }

    public void setUtilizationGroupC(StatResult.Simple utilizationGroupC) {
        this.utilizationGroupC = utilizationGroupC;
    }

    public StatResult.Simple getOrderTimeInSystem() {
        return orderTimeInSystem;
    }

    public void setOrderTimeInSystem(StatResult.Simple orderTimeInSystem) {
        this.orderTimeInSystem = orderTimeInSystem;
    }

    public StatResult.Simple getAllocatedDesksCount() {
        return allocatedDesksCount;
    }

    public void setAllocatedDesksCount(StatResult.Simple allocatedDesksCount) {
        this.allocatedDesksCount = allocatedDesksCount;
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

    private CarpenterResults rawToModel(Carpenter raw) {
        CarpenterResults r = new CarpenterResults(raw.getCarpenterId(), raw.getGroup().toString());
        r.setDeskID(raw.getCurrentDeskID());
        r.setAssignedOrderID(raw.getCurrentOrder() == null ? -1 : raw.getCurrentOrder().getOrderID());
        r.setOrderBT(raw.getWorkBT());
        r.setOrderET(raw.getWorkET());
        r.setOrderRepresentation(raw.getCurrentOrder() == null ? "" : raw.getCurrentOrder().toString());
        r.setWorking(raw.isWorking());
        return r;
    }

    private OrderResults rawToModel(FurnitureOrder raw) {
        OrderResults r = new OrderResults(raw.getOrderID(), raw.getDeskID(), raw.getTimeCreated(),
                raw.getProductType().toString(), raw.getStep().toString());
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
}
