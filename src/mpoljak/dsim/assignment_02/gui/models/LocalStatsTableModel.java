package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdEventResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LocalStatsTableModel extends AbstractTableModel {
    private final List<StatsModel> lResults;
    private final String[] aColNames = new String[] {
            "Stat name",
            "Value",
            "Unit" };
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public LocalStatsTableModel(List<StatsModel> lStats) {
        this.lResults = lStats;
    }

    public void updateTable(FurnitProdEventResults r) {
        lResults.clear();
        lResults.add(new StatsModel(r.getOrdersWaitingQueueCount().getDescription(),
                String.valueOf(r.getOrdersWaitingQueueCount().getValue()),
                r.getOrdersWaitingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersStainingQueueCount().getDescription(),
                String.valueOf(r.getOrdersStainingQueueCount().getValue()),
                r.getOrdersStainingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersAssemblingQueueCount().getDescription(),
                String.valueOf(r.getOrdersAssemblingQueueCount().getValue()),
                r.getOrdersAssemblingQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersFitInstQueueCount().getDescription(),
                String.valueOf(r.getOrdersFitInstQueueCount().getValue()),
                r.getOrdersFitInstQueueCount().getUnit()));
        lResults.add(new StatsModel(r.getOrdersWaitingQueueTime().getDescription(),
                String.valueOf(r.getOrdersWaitingQueueTime().getValue()/ FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersStainingQueueTime().getDescription(),
                String.valueOf(r.getOrdersStainingQueueTime().getValue()/ FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersAssemblingQueueTime().getDescription(),
                String.valueOf(r.getOrdersAssemblingQueueTime().getValue()/ FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getOrdersFitInstQueueTime().getDescription(),
                String.valueOf(r.getOrdersFitInstQueueTime().getValue()/ FurnitureProdForm.TIME_UNIT), "[h]"));
        lResults.add(new StatsModel(r.getUtilizationGroupA().getDescription(),
                String.valueOf(r.getUtilizationGroupA().getValue()*100), "%"));
        lResults.add(new StatsModel(r.getUtilizationGroupB().getDescription(),
                String.valueOf(r.getUtilizationGroupB().getValue()*100),"%"));
        lResults.add(new StatsModel(r.getUtilizationGroupC().getDescription(),
                String.valueOf(r.getUtilizationGroupC().getValue()*100),"%"));
        lResults.add(new StatsModel(r.getOrderTimeInSystem().getDescription(),
                String.valueOf(r.getOrderTimeInSystem().getValue()/ FurnitureProdForm.TIME_UNIT),"[h]"));
        lResults.add(new StatsModel(r.getAllocatedDesksCount().getDescription(),
                String.valueOf(r.getAllocatedDesksCount().getValue()),
                r.getAllocatedDesksCount().getUnit()));

        this.fireTableDataChanged();
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
        this.fireTableRowsUpdated(index, index);
    }

    public void remove(int index) {
        this.lResults.remove(index);
        fireTableRowsDeleted(index, index);
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
