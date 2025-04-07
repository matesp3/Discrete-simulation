package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OverallStatsTableModel extends AbstractTableModel {
    private final List<StatResult> lResults;
    private final String[] aColNames = new String[] {
            "Stat name",
            "Confidence interval: <left-bound | MEAN | right-bound>",
            "Unit" };
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public OverallStatsTableModel(List<StatResult> lVisits) {
        this.lResults = lVisits;
    }

    public void add(StatResult model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<StatResult> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public StatResult getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<StatResult> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, StatResult model) {
        if (model == null || index < 0 || index > this.lResults.size())
            return;
        this.lResults.set(index, model);
        this.fireTableDataChanged();
    }

    public void remove(int index) {
        this.lResults.remove(index);
        this.fireTableDataChanged();
    }

    public void clear() {
        this.lResults.clear();
        this.fireTableDataChanged();
    }


    @Override
    public String getColumnName(int column) {
        return this.aColNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.aColClasses[columnIndex];
    }

    @Override
    public int getRowCount() {
        return this.lResults.size();
    }

    @Override
    public int getColumnCount() {
        return this.aColNames.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StatResult stat = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return stat.getDescription();
        else if (columnIndex == 1)
            return stat.getValue();
        else if (columnIndex == 2)
            return stat.getUnit();
        return null;
    }
}
