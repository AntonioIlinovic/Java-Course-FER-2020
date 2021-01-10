package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.model.CalcModel;

import java.util.function.DoubleUnaryOperator;

/**
 * Unary operator class which gets CalcModel and DoubleUnaryOperator (that one it will get while initializing GUI)
 */
public class UnaryOperator {

    /**
     * Unary operator that will act on currently entered value in calculator (apply given operator to it, and display).
     */
    public static void apply(CalcModel model, DoubleUnaryOperator unaryOperator) {
        double result = unaryOperator.applyAsDouble(model.getValue());
        model.clear();
        model.setActiveOperand(result);
        model.freezeValue(String.valueOf(result));
    }

}
