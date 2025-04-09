package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdExpStats;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;
import mpoljak.dsim.utils.Formatter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OverallStatsTableModel extends AbstractTableModel {
    private final List<StatsModel> lResults;
    private final String[] aColNames = new String[] {
            "Stat name",
            "Confidence interval: <left-bound | MEAN | right-bound>",
            "Unit" };
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public OverallStatsTableModel(List<StatsModel> lVisits) {
        this.lResults = lVisits;
    }

    public void add(StatsModel model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<StatsModel> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public void updateTable(FurnitProdExpStats r) {
        lResults.clear();
        lResults.add(new StatsModel(r.getOrdersWaitingQueueCount().getDescription(),
                Formatter.getStrCI(r.getOrdersWaitingQueueCount().getHalfWidth(), r.getOrdersWaitingQueueCount().getMean(), 5, 1),
                r.getOrdersWaitingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersStainingQueueCount().getDescription(),
                Formatter.getStrCI(r.getOrdersStainingQueueCount().getHalfWidth(), r.getOrdersStainingQueueCount().getMean(), 5, 1),
                r.getOrdersStainingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersAssemblingQueueCount().getDescription(),
                Formatter.getStrCI(r.getOrdersAssemblingQueueCount().getHalfWidth(), r.getOrdersAssemblingQueueCount().getMean(), 5, 1),
                r.getOrdersAssemblingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersFitInstQueueCount().getDescription(),
                Formatter.getStrCI(r.getOrdersFitInstQueueCount().getHalfWidth(), r.getOrdersFitInstQueueCount().getMean(), 5, 1),
                r.getOrdersFitInstQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersWaitingQueueTime().getDescription(),
                Formatter.getStrCI(r.getOrdersWaitingQueueTime().getHalfWidth(), r.getOrdersWaitingQueueTime().getMean(), 5, FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersStainingQueueTime().getDescription(),
                Formatter.getStrCI(r.getOrdersStainingQueueTime().getHalfWidth(), r.getOrdersStainingQueueTime().getMean(), 5, FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersAssemblingQueueTime().getDescription(),
                Formatter.getStrCI(r.getOrdersAssemblingQueueTime().getHalfWidth(), r.getOrdersAssemblingQueueTime().getMean(), 5, FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersFitInstQueueTime().getDescription(),
                Formatter.getStrCI(r.getOrdersFitInstQueueTime().getHalfWidth(), r.getOrdersFitInstQueueTime().getMean(), 5, FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getUtilizationGroupA().getDescription(),
                Formatter.getStrCI(r.getUtilizationGroupA().getHalfWidth(), r.getUtilizationGroupA().getMean(), 5, 0.01),"%"));
        lResults.add(new StatsModel(r.getUtilizationGroupB().getDescription(),
                Formatter.getStrCI(r.getUtilizationGroupB().getHalfWidth(), r.getUtilizationGroupB().getMean(), 5, 0.01),"%"));
        lResults.add(new StatsModel(r.getUtilizationGroupC().getDescription(),
                Formatter.getStrCI(r.getUtilizationGroupC().getHalfWidth(), r.getUtilizationGroupC().getMean(), 5, 0.01),"%"));
        lResults.add(new StatsModel(r.getOrderTimeInSystem().getDescription(),
                Formatter.getStrCI(r.getOrderTimeInSystem().getHalfWidth(), r.getOrderTimeInSystem().getMean(), 5, FurnitureProdForm.TIME_UNIT),"[h]"));
//        lResults.add(new StatsModel(r.getAllocatedDesksCount().getDescription(),
//                Formatter.getStrCI(r.getAllocatedDesksCount().getMean(), r.getAllocatedDesksCount().getHalfWidth(), 5, 1),
//                r.getAllocatedDesksCount().getUnit()));

        this.fireTableDataChanged();
    }

    public StatsModel getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<StatsModel> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, StatsModel model) {
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
        StatsModel stat = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return stat.getDescription();
        else if (columnIndex == 1)
            return stat.getValue();
        else if (columnIndex == 2)
            return stat.getUnit();
        return null;
    }
}
