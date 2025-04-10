package mpoljak.dsim.common;

public class SimResults {
    private SimCommand resultsPrepCommand = null;
    private long experimentNum;

    public SimResults(long experimentNum) {
        this.experimentNum = experimentNum;
    }

    public long getExperimentNum() {
        return this.experimentNum;
    }

    public void setExperimentNum(long experimentNum) {
        this.experimentNum = experimentNum;
    }

    /**
     * If we want (as a delegate) to get relevant results of simulation, we need to call method {@code prepareResults()}
     * . This setter passes all actions to prepare simulation results for this instance.
     * @param resultsPrepCommand command that formats and prepares results.
     */
    public void setResultsPreparationCommand(SimCommand resultsPrepCommand) {
        this.resultsPrepCommand = resultsPrepCommand;
    }

    /**
     * In order to not bloat delegate of results with undesired frequent updates, this method is used as a solution to
     * avoid burden of results preparation if they're not desired in given moment. If results are wanted, this method
     * has to be called to get relevant results.
     */
    public void prepareResults() {
        if (resultsPrepCommand != null)
            resultsPrepCommand.invoke();
    }
}
