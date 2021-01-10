package hr.fer.oprpp1.gui.calc.buttons;

import hr.fer.oprpp1.gui.calc.BinaryOperator;
import hr.fer.oprpp1.gui.calc.UnaryOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Each InvertibleBinaryOperationButton has two texts for button (basic and inverted), and also two lambdas (basic and
 * inverted). It will be registered to inverseCheckBox so text and lambda will change accordingly. It has method invert
 * that will set current text and lambda according to the given boolean argument. When the button is pressed,
 * appropriate DoubleBinaryOperation will be executed.
 */
public class InvertibleBinaryOperationButton extends CalcButton {

    private boolean isInverted;
    private String currentText;
    private DoubleBinaryOperator currentDoubleBinaryOperator;

    public boolean invert(boolean isSelected) {
        isInverted = isSelected;

        currentText = isInverted ? textInverse : textBasic;
        currentDoubleBinaryOperator = isInverted ? doubleBinaryOperatorInverse : doubleBinaryOperatorBasic;
        setText(currentText);

        return isInverted;
    }

    String textBasic;
    String textInverse;
    DoubleBinaryOperator doubleBinaryOperatorBasic;
    DoubleBinaryOperator doubleBinaryOperatorInverse;

    public InvertibleBinaryOperationButton(String textBasic, String textInverse, CalcModel calculator,
                                           DoubleBinaryOperator doubleBinaryOperatorBasic, DoubleBinaryOperator doubleBinaryOperatorInverse) {
        super(textBasic);

        currentText = textBasic;
        currentDoubleBinaryOperator = doubleBinaryOperatorBasic;

        this.textBasic = textBasic;
        this.textInverse = textInverse;
        this.doubleBinaryOperatorBasic = doubleBinaryOperatorBasic;
        this.doubleBinaryOperatorInverse = doubleBinaryOperatorInverse;

        addActionListener(e -> {
            BinaryOperator.apply(calculator, currentDoubleBinaryOperator);
        });
    }
}
