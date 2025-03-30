package mpoljak.dsim.assignment_02.logic.furnitureStore.sim;

import java.util.Arrays;

public class FurnitureOrder {
    public enum FurnitureType {
        TABLE, CHAIR, WARDROBE
    }
    public enum TechStep {
        WOOD_PREPARATION, CARVING, STAINING, ASSEMBLING, FIT_INSTALLATION
    }
    private final double[] techStepsStart;
    private final double[] techStepsEnd;
    private final FurnitureType furnitureType;
    private final double timeOfCreation;

    public FurnitureOrder(double timeOfOrderCreation, FurnitureType furnitureType) {
        this.timeOfCreation = timeOfOrderCreation;
        this.furnitureType = furnitureType;
        this.techStepsStart = new double[5];
        this.techStepsEnd = new double[5];
        Arrays.fill(this.techStepsStart, -1);
        Arrays.fill(this.techStepsEnd, -1);
    }

    public void setTechStepStart(TechStep step, double time) {
        if (this.furnitureType != FurnitureType.WARDROBE && step == TechStep.FIT_INSTALLATION)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        this.techStepsStart[step.ordinal()] = time;
    }

    public void setTechStepEnd(TechStep step, double time) {
        if (this.furnitureType != FurnitureType.WARDROBE && step == TechStep.FIT_INSTALLATION)
            throw new IllegalArgumentException("FIT_INSTALLATION is used just for WARDROBE process.");
        this.techStepsEnd[step.ordinal()] = time;
    }

    /**
     * @param step for which step is duration wanted
     * @return time of tech step duration or <code>IllegalArgumentException</code> if this step hasn't been completed
     * yet or this step is not part of its creation process.
     */
    public double getTechStepDuration(TechStep step) {
       if (this.getLastValidIdx() < step.ordinal())
           throw new IllegalArgumentException("This step number is not part of "+this.furnitureType.name()+" process.");
        if (this.techStepsEnd[step.ordinal()] == -1)
            throw new IllegalArgumentException("Step hasn't been completed yet.");
        return this.techStepsEnd[step.ordinal()] - this.techStepsStart[step.ordinal()];
    }

    /**
     *
     * @param stepBefore identifies waiting after this step until next technological step was started. If
     *                   <code>null</code>, it identifies waiting time from order's creation until first technological
     *                   step start.
     * @return waiting time or <code>IllegalArgumentException</code> (step not completed or unreasonable param val).
     */
    public double getIntraWaitingDuration(TechStep stepBefore) {
        if (stepBefore == null)
            return this.techStepsStart[TechStep.WOOD_PREPARATION.ordinal()] - this.timeOfCreation;
        if (this.getLastValidIdx() <= stepBefore.ordinal())
            throw new IllegalArgumentException("Process is already finished or this step is not part of the process");
        if (this.techStepsStart[stepBefore.ordinal()+1] == -1)
            throw new IllegalArgumentException("Next step hasn't been completed yet.");
        return this.techStepsStart[stepBefore.ordinal()+1] - this.techStepsEnd[stepBefore.ordinal()];
    }

    /**
     * @return time of whole order processing or <code>RuntimeException</code>, if this order hasn't been
     * completely processed yet.
     */
    public double getOverallProcessingTime() {
        if (this.techStepsEnd[this.getLastValidIdx()] == -1)
            throw new RuntimeException("Order hasn't been completed yet.");
        return this.getTimeOfOrderCompletion() - this.timeOfCreation;
    }

    public FurnitureType getFurnitureType() {
        return this.furnitureType;
    }

    public double getTimeOfOrderCreation() {
        return this.timeOfCreation;
    }

    public double getTimeOfOrderCompletion() {
        return this.techStepsEnd[this.getLastValidIdx()];
    }

    @Override
    public String toString() {
        return String.format("Order{type=%s; arisen->completed=%.02f -> %.2f}",
        this.furnitureType, this.timeOfCreation, this.getTimeOfOrderCompletion());
    }

    private int getLastValidIdx() {
        return (this.furnitureType == FurnitureType.WARDROBE) ? TechStep.FIT_INSTALLATION.ordinal()
                : TechStep.ASSEMBLING.ordinal();
    }

    public static void main(String[] args) {
        FurnitureOrder order = new FurnitureOrder(2.0, FurnitureType.CHAIR);
        order.setTechStepStart(TechStep.WOOD_PREPARATION, 4.5);
        order.setTechStepEnd(TechStep.WOOD_PREPARATION, 5.8);
        order.setTechStepStart(TechStep.CARVING, 17);
        order.setTechStepEnd(TechStep.CARVING, 24);
        order.setTechStepStart(TechStep.STAINING, 27);
//        order.getOverallProcessingTime();
        System.out.println("Carving dur:"+order.getTechStepDuration(TechStep.CARVING));
//        System.out.println("Staining dur:"+order.getTechStepDuration(TechStep.STAINING));
        order.setTechStepEnd(TechStep.STAINING, 30);
        order.setTechStepStart(TechStep.ASSEMBLING, 32);
        order.setTechStepEnd(TechStep.ASSEMBLING, 40);
//        order.setTechStepStart(TechStep.FIT_INSTALLATION, 50);
//        order.setTechStepEnd(TechStep.FIT_INSTALLATION, 52);
        System.out.println("Intra dur:"+order.getIntraWaitingDuration(null));
        System.out.println("Complete dur:"+order.getOverallProcessingTime());
        System.out.println("Completion time:"+order.getTimeOfOrderCompletion());
        System.out.println(order);
    }
}
