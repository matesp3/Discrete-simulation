package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.common.SimResults;

public class FurnitProdExpStats extends SimResults {
    private StatResult.ConfInterval ordersWaitingQueueCount;
    private StatResult.ConfInterval ordersAssemblingQueueCount;
    private StatResult.ConfInterval ordersStainingQueueCount;
    private StatResult.ConfInterval ordersFitInstQueueCount;

    private StatResult.ConfInterval ordersWaitingQueueTime;
    private StatResult.ConfInterval ordersAssemblingQueueTime;
    private StatResult.ConfInterval ordersStainingQueueTime;
    private StatResult.ConfInterval ordersFitInstQueueTime;

    private StatResult.ConfInterval utilizationGroupA;
    private StatResult.ConfInterval utilizationGroupB;
    private StatResult.ConfInterval utilizationGroupC;

    private StatResult.ConfInterval orderTimeInSystem;
    private StatResult.ConfInterval allocatedDesksCount;

    public FurnitProdExpStats(long experimentNum) {
        super(experimentNum);
    }

    public StatResult.ConfInterval getOrdersWaitingQueueCount() {
        return ordersWaitingQueueCount;
    }

    public void setOrdersWaitingQueueCount(StatResult.ConfInterval ordersWaitingQueueCount) {
        this.ordersWaitingQueueCount = ordersWaitingQueueCount;
    }

    public StatResult.ConfInterval getOrdersAssemblingQueueCount() {
        return ordersAssemblingQueueCount;
    }

    public void setOrdersAssemblingQueueCount(StatResult.ConfInterval ordersAssemblingQueueCount) {
        this.ordersAssemblingQueueCount = ordersAssemblingQueueCount;
    }

    public StatResult.ConfInterval getOrdersStainingQueueCount() {
        return ordersStainingQueueCount;
    }

    public void setOrdersStainingQueueCount(StatResult.ConfInterval ordersStainingQueueCount) {
        this.ordersStainingQueueCount = ordersStainingQueueCount;
    }

    public StatResult.ConfInterval getOrdersFitInstQueueCount() {
        return ordersFitInstQueueCount;
    }

    public void setOrdersFitInstQueueCount(StatResult.ConfInterval ordersFitInstQueueCount) {
        this.ordersFitInstQueueCount = ordersFitInstQueueCount;
    }

    public StatResult.ConfInterval getOrdersWaitingQueueTime() {
        return ordersWaitingQueueTime;
    }

    public void setOrdersWaitingQueueTime(StatResult.ConfInterval ordersWaitingQueueTime) {
        this.ordersWaitingQueueTime = ordersWaitingQueueTime;
    }

    public StatResult.ConfInterval getOrdersAssemblingQueueTime() {
        return ordersAssemblingQueueTime;
    }

    public void setOrdersAssemblingQueueTime(StatResult.ConfInterval ordersAssemblingQueueTime) {
        this.ordersAssemblingQueueTime = ordersAssemblingQueueTime;
    }

    public StatResult.ConfInterval getOrdersStainingQueueTime() {
        return ordersStainingQueueTime;
    }

    public void setOrdersStainingQueueTime(StatResult.ConfInterval ordersStainingQueueTime) {
        this.ordersStainingQueueTime = ordersStainingQueueTime;
    }

    public StatResult.ConfInterval getOrdersFitInstQueueTime() {
        return ordersFitInstQueueTime;
    }

    public void setOrdersFitInstQueueTime(StatResult.ConfInterval ordersFitInstQueueTime) {
        this.ordersFitInstQueueTime = ordersFitInstQueueTime;
    }

    public StatResult.ConfInterval getUtilizationGroupA() {
        return utilizationGroupA;
    }

    public void setUtilizationGroupA(StatResult.ConfInterval utilizationGroupA) {
        this.utilizationGroupA = utilizationGroupA;
    }

    public StatResult.ConfInterval getUtilizationGroupB() {
        return utilizationGroupB;
    }

    public void setUtilizationGroupB(StatResult.ConfInterval utilizationGroupB) {
        this.utilizationGroupB = utilizationGroupB;
    }

    public StatResult.ConfInterval getUtilizationGroupC() {
        return utilizationGroupC;
    }

    public void setUtilizationGroupC(StatResult.ConfInterval utilizationGroupC) {
        this.utilizationGroupC = utilizationGroupC;
    }

    public StatResult.ConfInterval getOrderTimeInSystem() {
        return orderTimeInSystem;
    }

    public void setOrderTimeInSystem(StatResult.ConfInterval orderTimeInSystem) {
        this.orderTimeInSystem = orderTimeInSystem;
    }

    public StatResult.ConfInterval getAllocatedDesksCount() {
        return allocatedDesksCount;
    }

    public void setAllocatedDesksCount(StatResult.ConfInterval allocatedDesksCount) {
        this.allocatedDesksCount = allocatedDesksCount;
    }
}
