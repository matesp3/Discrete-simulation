package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.gui.models.LocalStatsTableModel;
import mpoljak.dsim.assignment_02.gui.models.OverallStatsTableModel;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;
import mpoljak.dsim.utils.SwingTableColumnResizer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class StatsViewer extends JPanel {
    private OverallStatsTableModel overallStatsTableModel;
    private LocalStatsTableModel localStatsTableModel;
    private final JScrollPane scrollPane;
    private JPanel contentPane;
    private JTable resultsLocalJTab;
    private JTable resultsAllJTab;

    public StatsViewer() {
        this.contentPane = createTables();
        this.scrollPane = new JScrollPane(this.contentPane);
        this.scrollPane.setWheelScrollingEnabled(true);
        this.add(scrollPane);
//        this.setBackground(FurnitureProdForm.COL_BG_CONTENT);
    }

    private JPanel createTables() {
        JPanel content = new JPanel();
        content.setBackground(FurnitureProdForm.COL_BG_TAB);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel p0 = new JPanel();
        p0.setBackground(FurnitureProdForm.COL_BG_TAB);
        p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));

        this.overallStatsTableModel = new OverallStatsTableModel(new ArrayList<>());
        this.resultsAllJTab = new JTable(overallStatsTableModel);
        resultsAllJTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane statScrollPane1 = new JScrollPane(resultsAllJTab);
        statScrollPane1.setPreferredSize(new Dimension(500,500));
        statScrollPane1.setMinimumSize(new Dimension(500, 150));
        statScrollPane1.setMaximumSize(new Dimension(1000, 200));

        this.localStatsTableModel = new LocalStatsTableModel(new ArrayList<>());
        this.resultsLocalJTab = new JTable(localStatsTableModel);
//        SwingTableColumnResizer.setJTableColsWidth(resultsJTab, width - 40, new double[] {37,57,6});
        resultsLocalJTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane statScrollPane2 = new JScrollPane(resultsLocalJTab);
        statScrollPane2.setPreferredSize(new Dimension(500,500));
        statScrollPane2.setMinimumSize(new Dimension(500, 150));
        statScrollPane2.setMaximumSize(new Dimension(1000, 200));


        p0.add(statScrollPane1);
        p0.add(Box.createRigidArea(new Dimension(25,0)));
        p0.add(statScrollPane2);
        content.add(p0);
        return content;
    }

    public void addStatResult(StatResult statResult) {
        this.overallStatsTableModel.add(statResult);
    }

    public void updateStats(List<StatResult> resultList) {
        this.overallStatsTableModel.setModels(resultList);
    }

    public void updateStatAtRow(int rowIdx, StatResult statResult) {
        this.overallStatsTableModel.setModel(rowIdx, statResult);
    }

    public void clearStatsList() {
        this.overallStatsTableModel.clear();
    }

    public void resizeContent(int width, int height) {
        this.scrollPane.setPreferredSize(new Dimension(width, height));
        SwingTableColumnResizer.setJTableColsWidth(this.resultsAllJTab, (width-25)/2, new double[] {45,50,5});
        SwingTableColumnResizer.setJTableColsWidth(this.resultsLocalJTab, (width-25)/2, new double[] {45,50,5});
        this.repaint();
    }
}
