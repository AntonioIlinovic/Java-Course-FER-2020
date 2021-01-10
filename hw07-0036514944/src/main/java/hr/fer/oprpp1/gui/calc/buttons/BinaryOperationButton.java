package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.BinaryOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

import javax.swing.*;
import java.util.function.DoubleBinaryOperator;

public class BinaryOperationButton extends CalcButton {

    public BinaryOperationButton(String text, CalcModel calculator, DoubleBinaryOperator binaryOperator) {
        super(text);
        addActionListener(e -> BinaryOperator.apply(calculator, binaryOperator));
    }

}
