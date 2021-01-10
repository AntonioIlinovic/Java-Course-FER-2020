package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.IOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

/**
 * Digit Button class that will be used in buttons for entering buttons 0-9. Only difference between its extended class
 * is that it will have bigger font. It will have IOperator that will insert appropriate digit to given calculator
 * model.
 */
public class DigitButton extends OperationButton {

    public DigitButton(String text, CalcModel calculator, IOperator operator) {
        super(text, calculator, operator);
        setFont(getFont().deriveFont(30f));
    }
}
