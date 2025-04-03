package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.models.OverallStatsTableModel;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;
import mpoljak.dsim.utils.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class OverallStatsViewer extends JPanel {
    private final OverallStatsTableModel tableModel;

    public OverallStatsViewer(int width, int height) {
        this.setSize(width, height);
        this.tableModel = new OverallStatsTableModel(new ArrayList<>());
        JTable resultsJTab = new JTable(tableModel);
        SwingTableColumnResizer.setJTableColsWidth(resultsJTab, width - 40, new double[] {37,57,6});
        resultsJTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultsJTab);
        scrollPane.setPreferredSize(new Dimension(width - 40,height - 20));
        this.add(scrollPane);
//        this.setBackground(FurnitureProdForm.COL_BG_CONTENT);
    }

    public void addStatResult(StatResult statResult) {
        this.tableModel.add(statResult);
    }

    public void updateStatsList(List<StatResult> resultList) {
        this.tableModel.setModels(resultList);
    }

    public void updateStatAtRow(int rowIdx, StatResult statResult) {
        this.tableModel.setModel(rowIdx, statResult);
    }

    public void clearStatsList() {
        this.tableModel.clear();
    }
}
