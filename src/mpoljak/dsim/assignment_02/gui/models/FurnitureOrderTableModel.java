package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.logic.furnitureStore.results.OrderResults;
import mpoljak.dsim.utils.Formatter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class FurnitureOrderTableModel extends AbstractTableModel {

    private final String[] aColNames = new String[] {
            "OrderID",
            "DeskID",
            "Product",
            "step",
            "waitingBT",
            "stepBT",
            "stepET",
            "created"};
    private final List<OrderResults> lResults;
    private final Class<?>[] aColClasses = new Class<?>[] {
            Integer.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,};

    public FurnitureOrderTableModel(List<OrderResults> lResults) {
        this.lResults = lResults;
    }

    public void add(OrderResults model) {
        this.lResults.add(model);
        this.fireTableDataChanged();
    }

    public void setModels(List<OrderResults> lModels) {
        this.clear();
        if (lModels != null)
            lResults.addAll(lModels);
        this.fireTableDataChanged();
    }

    public OrderResults getModel(int index) {
        return this.lResults.get(index);
    }

    public ArrayList<OrderResults> getModels() {
        return new ArrayList<>(this.lResults);
    }

    public void setModel(int index, OrderResults model) {
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
        OrderResults order = this.lResults.get(rowIndex);
        if (columnIndex == 0)
            return order.getOrderID();
        else if (columnIndex == 1)
            return order.getDeskID();
        else if (columnIndex == 2)
            return order.getProductType();
        else if (columnIndex == 3)
            return order.getStep();
        else if (columnIndex == 4)
            return Formatter.getStrDateTime(order.getWaitingBT(),8, 6);
        else if (columnIndex == 5)
            return Formatter.getStrDateTime(order.getStepStart(), 8, 6);
        else if (columnIndex == 6)
            return Formatter.getStrDateTime(order.getStepEnd(), 8, 6);
        else if (columnIndex == 7)
            return Formatter.getStrDateTime(order.getCreated(), 8, 6);
        return null;
    }
}
