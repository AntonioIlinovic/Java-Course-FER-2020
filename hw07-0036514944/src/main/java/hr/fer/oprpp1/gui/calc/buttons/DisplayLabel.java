package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.model.CalcModel;

import javax.swing.*;
import java.awt.*;

/**
 * Label that will act as a display of a given calculator.
 */
public class DisplayLabel extends JLabel {

    public DisplayLabel(String text, CalcModel calculator) {
        super(text, SwingConstants.RIGHT);
        calculator.addCalcValueListener(model -> setText(calculator.toString()));
        setFont(getFont().deriveFont(30f));
        setBackground(Color.decode("#ffff00"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setOpaque(true);
    }

}
