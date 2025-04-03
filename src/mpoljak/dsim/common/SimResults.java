package mpoljak.dsim.common;

public class SimResults {
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
}
