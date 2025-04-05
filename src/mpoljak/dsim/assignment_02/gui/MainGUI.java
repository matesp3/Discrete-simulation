package mpoljak.dsim.assignment_02.gui;

import mpoljak.dsim.assignment_02.logic.EventSim;
import mpoljak.dsim.assignment_02.logic.furnitureStore.sim.FurnitureProductionSim;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class MainGUI {
    // look & feel
    final static String LOOK_AND_FEEL = "GTK"; // null (use the default), "Metal", "System", "Motif" and "GTK"
    final static String THEME = "DefaultMetal"; // For Metal L&F, themes: "DefaultMetal", "Ocean"

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                initLookAndFeel();
//                JFrame.setDefaultLookAndFeelDecorated(true); // nice window decorations
                FurnitureProdForm form = new FurnitureProdForm();
            }
        });
    }

    private static void initLookAndFeel() {
        String lookAndFeel = null;
        if (LOOK_AND_FEEL != null) {
            if (LOOK_AND_FEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            else if (LOOK_AND_FEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            }
            else if (LOOK_AND_FEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            else if (LOOK_AND_FEEL.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }
            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                        + LOOK_AND_FEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                // If L&F = "Metal", set the theme
                if (LOOK_AND_FEEL.equals("Metal")) {
                    if (THEME.equals("DefaultMetal"))
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    else // (THEME.equals("Ocean"))
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                }

            }
            catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class for specified look and feel:"
                        + lookAndFeel);
                System.err.println("Did you include the L&F library in the class path?");
                System.err.println("Using the default look and feel.");
            }
            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                        + lookAndFeel
                        + ") on this platform.");
                System.err.println("Using the default look and feel.");
            }
            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                        + lookAndFeel
                        + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }
}
