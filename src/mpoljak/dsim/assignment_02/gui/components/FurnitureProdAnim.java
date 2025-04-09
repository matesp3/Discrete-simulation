package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;
import mpoljak.dsim.assignment_02.gui.models.CarpenterTableModel;
import mpoljak.dsim.assignment_02.gui.models.FurnitureOrderTableModel;
import mpoljak.dsim.assignment_02.logic.furnitureStore.results.FurnitProdEventResults;
import mpoljak.dsim.utils.SwingTableColumnResizer;
import mpoljak.dsim.utils.TimeFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FurnitureProdAnim extends JPanel {
    private ResultViewer viewSimTime;
    private CarpenterTableModel carpenterTableModelA;
    private CarpenterTableModel carpenterTableModelB;
    private CarpenterTableModel carpenterTableModelC;
    private FurnitureOrderTableModel orderTableModelWaiting;
    private FurnitureOrderTableModel orderTableModelStaining;
    private FurnitureOrderTableModel orderTableModelAssembling;
    private FurnitureOrderTableModel orderTableModelFitInst;

    private JTable carpenterTableA;
    private JTable carpenterTableB;
    private JTable carpenterTableC;
    private JTable orderTableWaiting;
    private JTable orderTableStaining;
    private JTable orderTableAssembling;
    private JTable orderTableFitInst;
    private JScrollPane mainScrollPane;
    private JPanel contentPane;

    public FurnitureProdAnim() {
        this.viewSimTime = new ResultViewer("Simulation Time");
        this.contentPane = this.createTables();
        this.mainScrollPane = new JScrollPane(this.contentPane);
        this.mainScrollPane.setWheelScrollingEnabled(true);
        this.add(this.mainScrollPane);
    }

    public void setSimTime(double minutes) {
         this.viewSimTime.setValue(minutes, 0);
    }

    public void resizeContent(int width, int height) {
        this.mainScrollPane.setPreferredSize(new Dimension(width, height));
        SwingTableColumnResizer.setJTableColsWidth(this.orderTableWaiting, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.orderTableStaining, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.orderTableAssembling, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.orderTableFitInst, (width-25)/2,
                new double[] {5,5,5,15,19,17,17,17});
        SwingTableColumnResizer.setJTableColsWidth(this.carpenterTableA, width,
                new double[] {5,5,5,5,10,15,15,40});
        SwingTableColumnResizer.setJTableColsWidth(this.carpenterTableB, width,
                new double[] {5,5,5,5,10,15,15,40});
        SwingTableColumnResizer.setJTableColsWidth(this.carpenterTableC, width,
                new double[] {5,5,5,5,10,15,15,40});
    }

    private JPanel createTables() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p0 = new JPanel();
        p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));
        p0.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p2.setBackground(FurnitureProdForm.COL_BG_TAB);
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
        p3.setBackground(FurnitureProdForm.COL_BG_TAB);

        this.carpenterTableModelA = new CarpenterTableModel(new ArrayList<>());
        this.carpenterTableA = new JTable(this.carpenterTableModelA);
        carpenterTableA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneA = new JScrollPane(carpenterTableA);
        carpenterScrollPaneA.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneA.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneA.setMaximumSize(new Dimension(2000, 200));

        this.carpenterTableModelB = new CarpenterTableModel(new ArrayList<>());
        this.carpenterTableB = new JTable(this.carpenterTableModelB);
        carpenterTableB.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneB = new JScrollPane(carpenterTableB);
        carpenterScrollPaneB.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneB.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneB.setMaximumSize(new Dimension(2000, 200));

        this.carpenterTableModelC = new CarpenterTableModel(new ArrayList<>());
        this.carpenterTableC = new JTable(this.carpenterTableModelC);
        carpenterTableC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane carpenterScrollPaneC = new JScrollPane(carpenterTableC);
        carpenterScrollPaneC.setPreferredSize(new Dimension(1000,150));
        carpenterScrollPaneC.setMinimumSize(new Dimension(500, 150));
        carpenterScrollPaneC.setMaximumSize(new Dimension(2000, 200));

        this.orderTableModelWaiting = new FurnitureOrderTableModel(new ArrayList<>());
        this.orderTableWaiting = new JTable(this.orderTableModelWaiting);
        orderTableWaiting.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneWaiting = new JScrollPane(orderTableWaiting);
        orderScrollPaneWaiting.setPreferredSize(new Dimension(500,150));
        orderScrollPaneWaiting.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneWaiting.setMaximumSize(new Dimension(1000, 200));

        this.orderTableModelStaining = new FurnitureOrderTableModel(new ArrayList<>());
        this.orderTableStaining = new JTable(this.orderTableModelStaining);
        orderTableStaining.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneStaining = new JScrollPane(orderTableStaining);
        orderScrollPaneStaining.setPreferredSize(new Dimension(500,150));
        orderScrollPaneStaining.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneStaining.setMaximumSize(new Dimension(1000, 200));

        this.orderTableModelAssembling = new FurnitureOrderTableModel(new ArrayList<>());
        this.orderTableAssembling = new JTable(this.orderTableModelAssembling);
        orderTableAssembling.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneAssembling = new JScrollPane(orderTableAssembling);
        orderScrollPaneAssembling.setPreferredSize(new Dimension(500,150));
        orderScrollPaneAssembling.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneAssembling.setMaximumSize(new Dimension(1000, 200));

        this.orderTableModelFitInst = new FurnitureOrderTableModel(new ArrayList<>());
        this.orderTableFitInst = new JTable(this.orderTableModelFitInst);
        orderTableFitInst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane orderScrollPaneFitInst = new JScrollPane(orderTableFitInst);
        orderScrollPaneFitInst.setPreferredSize(new Dimension(500,150));
        orderScrollPaneFitInst.setMinimumSize(new Dimension(500, 150));
        orderScrollPaneFitInst.setMaximumSize(new Dimension(1000, 200));

        JLabel jl1A = new JLabel("Order queue for Waiting");
        jl1A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl1B = new JLabel("Order queue for Staining");
        jl1B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2A = new JLabel("Order queue for Assembling");
        jl2A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl2B = new JLabel("Order queue for Fit installation");
        jl2B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3A = new JLabel("Carpenter's group A");
        jl3A.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3B = new JLabel("Carpenter's group B");
        jl3B.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        JLabel jl3C = new JLabel("Carpenter's group C");
        jl3C.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);

        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(jl1A);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(orderScrollPaneWaiting);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(jl1B);
        p1.add(Box.createRigidArea(new Dimension(0, 7)));
        p1.add(orderScrollPaneStaining);

        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(jl2A);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(orderScrollPaneAssembling);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(jl2B);
        p2.add(Box.createRigidArea(new Dimension(0, 7)));
        p2.add(orderScrollPaneFitInst);

        p3.add(jl3A);
        p3.add(carpenterScrollPaneA);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(jl3B);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(carpenterScrollPaneB);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(jl3C);
        p3.add(Box.createRigidArea(new Dimension(0, 7)));
        p3.add(carpenterScrollPaneC);

        p0.add(p1);
        p0.add(Box.createRigidArea(new Dimension(25,0)));
        p0.add(p2);
        this.viewSimTime.setBorder(BorderFactory.createRaisedBevelBorder());
        this.viewSimTime.setMaximumSize(new Dimension(2000, 50));
        this.viewSimTime.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(this.viewSimTime);
        content.add(p0);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(p3);
        return content;
    }

    public void setEventResultsModel(FurnitProdEventResults r) {
        this.orderTableModelWaiting.setModels(r.getOrdersA());
        this.orderTableModelAssembling.setModels(r.getOrdersB());
        this.orderTableModelStaining.setModels(r.getOrdersCLow());
        this.orderTableModelFitInst.setModels(r.getOrdersCHigh());
        this.viewSimTime.setValue(TimeFormatter.getStrDateTime(r.getSimTime()*60, 8, 6));
        this.carpenterTableModelA.setModels(r.getCarpentersA());
        this.carpenterTableModelB.setModels(r.getCarpentersB());
        this.carpenterTableModelC.setModels(r.getCarpentersC());
    }



    public static void main(String[] args) {
        double secs = 0;
        for (int i = 0; i < 3600*9; i++) {
            System.out.println(TimeFormatter.getStrDateTime(secs++, 8, 6)); // ok
        }
    }
}
