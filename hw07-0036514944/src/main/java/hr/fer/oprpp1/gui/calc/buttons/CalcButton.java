package hr.fer.oprpp1.gui.calc.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * Type of JButton that all buttons in calculator will use. It sets the border, background color and text.
 */
public class CalcButton extends JButton {

    CalcButton(String text) {
        super(text);
        setBackground(Color.decode("#f2e6ff"));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setOpaque(true);
    }

}
