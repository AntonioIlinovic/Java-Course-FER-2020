package hr.fer.oprpp1.gui.calc.buttons;

import java.awt.event.ActionListener;

/**
 * Class that will add given ActionListener to this button. It is used in POP and PUSH buttons.
 */
public class SpecialButton extends CalcButton {

    public SpecialButton(String text, ActionListener l) {
        super(text);
        addActionListener(l);
    }

}
