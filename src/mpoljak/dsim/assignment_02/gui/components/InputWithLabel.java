package mpoljak.dsim.assignment_02.gui.components;

import mpoljak.dsim.assignment_02.gui.FurnitureProdForm;

import javax.swing.*;

public class InputWithLabel extends JPanel {
    private final JLabel label;
    private final JTextField textField;

    public InputWithLabel(String desc, int expectedLettersCount, String hintText) {
        this.setBackground(FurnitureProdForm.COL_BG);
        this.label = new JLabel(desc);
        this.label.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        this.textField = new JTextField(hintText);
        this.textField.setForeground(FurnitureProdForm.COL_TEXT_FONT_1);
        this.textField.setColumns( (int)((5.0/7.0)*expectedLettersCount)+1 );
        this.add(label);
        this.add(textField);
    }

    public int getIntValue() {
        return Integer.parseInt(textField.getText());
    }

    public double getDoubleValue() {
        return Double.parseDouble(textField.getText());
    }

    public String getStringValue() {
        return textField.getText().trim();
    }

    public void setEnabled(boolean enabled) {
        this.textField.setEnabled(enabled);
    }

}
