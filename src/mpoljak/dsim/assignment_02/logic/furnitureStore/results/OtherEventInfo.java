package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

import mpoljak.dsim.common.SimResults;

public class OtherEventInfo extends SimResults {
    private String message;

    public OtherEventInfo(long experimentNum, String message) {
        super(experimentNum);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
