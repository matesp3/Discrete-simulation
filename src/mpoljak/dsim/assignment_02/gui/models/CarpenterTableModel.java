package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.logic.furnitureStore.results.CarpenterResults;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarpenterTableModel extends AbstractTableModel {
    private final List<CarpenterResults> lResults;
    private final String[] aColNames = new String[] {
            "ID",
            "Group",
            "DeskID",
            "OrderID",
            "ProcBT",
            "ProcET",
            "Working"};
    private final Class<?>[] aColClasses = new Class<?>[] {
            Integer.class,
            String.class,
            Integer.class,
            Integer.class,
            Double.class,
            Double.class,
            Boolean.class,
    };

    public CarpenterTableModel(List<CarpenterResults> lResults) {
        this.lResults = lResults;
    }

    public void add(CarpenterResults model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<CarpenterResults> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public CarpenterResults getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<CarpenterResults> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, CarpenterResults model) {
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
        CarpenterResults c = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return c.getCarpenterID();
        if (columnIndex == 1)
            return c.getGroup();
        if (columnIndex == 2)
            return c.getDeskID();
        if (columnIndex == 3)
            return c.getAssignedOrderID();
        if (columnIndex == 4)
            return c.getOrderBT();
        if (columnIndex == 5)
            return c.getOrderET();
        if (columnIndex == 6)
            return c.isWorking();
        return null;
    }
}
