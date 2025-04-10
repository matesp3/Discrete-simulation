package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.gui.models.LocalStatsTableModel;
import mpoljak.dsim.assignment_02.gui.models.OverallStatsTableModel;
import mpoljak.dsim.assignment_02.gui.models.StatsModel;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdEventResults;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdExpStats;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.StatResult;
import mpoljak.dsim.utils.SwingTableColumnResizer;
import mpoljak.dsim.utils.Formatter;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class StatsViewer extends JPanel {
    private ResultViewer expTimeViewer;
    private OverallStatsTableModel overallStatsTableModel;
    private LocalStatsTableModel localStatsTableModel;
    private final JScrollPane scrollPane;
    private JPanel contentPane;
    private JTable resultsLocalJTab;
    private JTable resultsAllJTab;

    public StatsViewer() {
        this.expTimeViewer = new ResultViewer("Time in experiment", "0");

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
        JPanel p1 = new JPanel();
        p1.setBackground(FurnitureProdForm.COL_BG_TAB);
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        JPanel p2 = new JPanel();
        p2.setBackground(FurnitureProdForm.COL_BG_TAB);
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

        this.overallStatsTableModel = new OverallStatsTableModel(new ArrayList<>());
        this.resultsAllJTab = new JTable(overallStatsTableModel);
        resultsAllJTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane statScrollPane1 = new JScrollPane(resultsAllJTab);
        statScrollPane1.setPreferredSize(new Dimension(500,300));
        statScrollPane1.setMinimumSize(new Dimension(500, 150));
        statScrollPane1.setMaximumSize(new Dimension(1000, 300));

        this.localStatsTableModel = new LocalStatsTableModel(new ArrayList<>());
        this.resultsLocalJTab = new JTable(localStatsTableModel);
//        SwingTableColumnResizer.setJTableColsWidth(resultsJTab, width - 40, new double[] {37,57,6});
        resultsLocalJTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane statScrollPane2 = new JScrollPane(resultsLocalJTab);
        statScrollPane2.setPreferredSize(new Dimension(500,300));
        statScrollPane2.setMinimumSize(new Dimension(500, 150));
        statScrollPane2.setMaximumSize(new Dimension(1000, 300));

        this.expTimeViewer.setPreferredSize(new Dimension(500, 30));
        this.expTimeViewer.setMinimumSize(new Dimension(500, 30));
        this.expTimeViewer.setMaximumSize(new Dimension(1000, 30));
        this.expTimeViewer.setBorder(BorderFactory.createRaisedBevelBorder());
        this.expTimeViewer.setAlignmentY(CENTER_ALIGNMENT);

        JLabel jl1 = new JLabel("Overall Stats");
        jl1.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2 = new JLabel("Stats within one experiment (local)");
        jl2.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        p1.add(Box.createRigidArea(new Dimension(0,47)));
        p1.add(jl1);
        p1.add(Box.createRigidArea(new Dimension(0,7)));
        p1.add(statScrollPane1);
        p2.add(Box.createRigidArea(new Dimension(0,10)));
        p2.add(jl2);
        p2.add(Box.createRigidArea(new Dimension(0,7)));
        p2.add(this.expTimeViewer);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(statScrollPane2);

        p0.add(p1);
        p0.add(Box.createRigidArea(new Dimension(25,0)));
        p0.add(p2);
        content.add(p0);
        return content;
    }

    public void updateExperimentTime(double time) {
        this.expTimeViewer.setValue(Formatter.getStrDateTime(time, 8, 6));
    }

    public void addStatResult(StatsModel statResult) {
        this.overallStatsTableModel.add(statResult);
    }

    public void updateOverallStats(FurnitProdExpStats results) {
        this.overallStatsTableModel.updateTable(results);
    }

    public void updateLocalStats(FurnitProdEventResults results) {
        this.localStatsTableModel.updateTable(results);
    }

    public void updateStatAtRow(int rowIdx, StatsModel statResult) {
        this.overallStatsTableModel.setModel(rowIdx, statResult);
    }

    public void clearStatsList() {
        this.overallStatsTableModel.clear();
        this.localStatsTableModel.clear();
    }

    public void resizeContent(int width, int height) {
        this.scrollPane.setPreferredSize(new Dimension(width, height));
        SwingTableColumnResizer.setJTableColsWidth(this.resultsAllJTab, (width-25)/2, new double[] {45,50,5});
        SwingTableColumnResizer.setJTableColsWidth(this.resultsLocalJTab, (width-25)/2, new double[] {45,50,5});
        this.repaint();
    }
}
