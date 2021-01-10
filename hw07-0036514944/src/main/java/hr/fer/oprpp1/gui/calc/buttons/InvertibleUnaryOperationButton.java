package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.UnaryOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

import javax.swing.*;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Each InvertibleUnaryOperationButton has two texts for button (basic and inverted), and also two lambdas (basic and
 * inverted). It will be registered to inverseCheckBox so text and lambda will change accordingly. It has method invert
 * that will set current text and lambda according to the given boolean argument. When the button is pressed,
 * appropriate DoubleUnaryOperation will be executed.
 */
public class InvertibleUnaryOperationButton extends CalcButton {

    private String currentText;
    private DoubleUnaryOperator currentDoubleUnaryOperator;

    public boolean invert(boolean isSelected) {

        currentText = isSelected ? textInverse : textBasic;
        currentDoubleUnaryOperator = isSelected ? doubleUnaryOperatorInverse : doubleUnaryOperatorBasic;
        setText(currentText);

        return isSelected;
    }

    String textBasic;
    String textInverse;
    DoubleUnaryOperator doubleUnaryOperatorBasic;
    DoubleUnaryOperator doubleUnaryOperatorInverse;

    public InvertibleUnaryOperationButton(String textBasic, String textInverse, CalcModel calculator,
                                          DoubleUnaryOperator doubleUnaryOperatorBasic, DoubleUnaryOperator doubleUnaryOperatorInverse) {
        super(textBasic);

        currentText = textBasic;
        currentDoubleUnaryOperator = doubleUnaryOperatorBasic;

        this.textBasic = textBasic;
        this.textInverse = textInverse;
        this.doubleUnaryOperatorBasic = doubleUnaryOperatorBasic;
        this.doubleUnaryOperatorInverse = doubleUnaryOperatorInverse;

        addActionListener(e -> {
            UnaryOperator.apply(calculator, currentDoubleUnaryOperator);
        });
    }

}
