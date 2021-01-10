package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.IOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

import javax.swing.*;
import java.awt.*;

/**
 * Operation Button will accept IOperator that will be applied everytime button is pressed
 */
public class OperationButton extends CalcButton {

    public OperationButton(String text, CalcModel calculator, IOperator operator) {
        super(text);
        addActionListener(e -> operator.apply(calculator));
    }
    
}
