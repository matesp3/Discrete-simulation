package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.Carpenter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarpenterTableModel extends AbstractTableModel {
    private final List<Carpenter> lResults;
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

    public CarpenterTableModel(List<Carpenter> lResults) {
        this.lResults = lResults;
    }

    public void add(Carpenter model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<Carpenter> lModels) {
        this.clear();
        lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public Carpenter getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<Carpenter> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, Carpenter model) {
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
        Carpenter c = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return c.getCarpenterId();
        if (columnIndex == 1)
            return c.getGroup();
        if (columnIndex == 2)
            return c.getCurrentDeskID();
        if (columnIndex == 3)
            return c.getCurrentOrder();
        if (columnIndex == 4)
            return c.getOrderProcessingBT();
        if (columnIndex == 5)
            return c.getOrderProcessingET();
        if (columnIndex == 6)
            return c.isWorking();
        return null;
    }
}
