package mpoljak.dsim.assignment_02.logic.furnitureStore.results;

public class StatResult {
    private String description;
    private String value;
    private String unit;

    public StatResult() {
        this("", "", "");
    }

    public StatResult(String description, String value, String unit) {
        this.description = description;
        this.value = value;
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
