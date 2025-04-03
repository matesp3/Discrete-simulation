package mpoljak.dsim.assignment_02.gui.models;

import mpoljak.dsim.assignment_02.logic.furnitureStore.results.OrderResults;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FurnitureOrderTableModel extends AbstractTableModel {

    private final String[] aColNames = new String[] {
            "Stat name",
            "Confidence interval: <left-bound | MEAN | right-bound>",
            "Unit" };
    private final List<OrderResults> lResults;
    private final Class<?>[] aColClasses = new Class<?>[] {
            String.class,
            String.class,
            String.class };

    public FurnitureOrderTableModel(List<OrderResults> lResults) {
        this.lResults = lResults;
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
