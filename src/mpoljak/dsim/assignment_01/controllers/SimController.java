package mpoljak.dsim.assignment_01.controllers;

public class SimController {
    private static final String[] STRATEGIES = {"strategy A", "strategy B", "strategy C", "strategy D",
            "own strategy 1", "custom strategy"};
    private static final int DEFAULT_STRATEGY = 0;

    /**
     * @return IDs of all existing strategies, that can be used
     */
    public static String[] getStrategies() {
        String[] strategs = new String[STRATEGIES.length];
        System.arraycopy(STRATEGIES, 0, strategs, 0, STRATEGIES.length);
        return strategs;
    }

    public static String getDefaultStrategyID() {
        return STRATEGIES[DEFAULT_STRATEGY];
    }

    private int strategy;

    public SimController() {
        this.strategy = DEFAULT_STRATEGY;
    }

    /**
     * Sets strategy corresponding to unique strategy's ID specified by <code>strategyID</code> parameter which
     * has to contain one of values retrieved from method <code>SimController.getStrategies()</code>.
     * @param strategyID strategy ID. All possible IDs can be retrieved by calling
     *                      <code>SimController.getStrategies()</code>.
     */
    public void setStrategy(String strategyID) {
        for (int i = 0; i < STRATEGIES.length; i++) {
            if (strategyID.compareTo(STRATEGIES[i])==0) {
                this.strategy = i;
                break;
            }
        }
        System.out.println("Set Strategy: "+ this.strategy+". ID = "+strategyID);
    }
}
