package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.model.CalcModel;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;

/**
 * Unary operator class which gets CalcModel and DoubleBinaryOperator (that one it will get while initializing GUI)
 */
public class BinaryOperator {

    /**
     * Depending on the state of calculator. If active operand is already set (meaning user already entered one operand
     * and operation that waits to be executed (for example +, -, *, / ...)) given binaryOperator will just be set as an
     * PendingBinaryOperation (it will wait for user to enter the second operand and after that it will be applied to
     * both operands). If there is PendingBinaryOperator already set, it will apply that operator to both operands.
     * <p>
     * After that it will display the result, and be ready to accept new value or operator.
     */
    public static void apply(CalcModel model, DoubleBinaryOperator doubleBinaryOperator) {
        double result = model.isActiveOperandSet() ?
                model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue())
                :
                model.getValue();
        model.clear();
        model.setActiveOperand(result);
        model.setPendingBinaryOperation(doubleBinaryOperator);
        model.freezeValue(String.valueOf(result));
    }

}
