package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.IOperator;
import hr.fer.oprpp1.gui.calc.model.CalcModel;

/**
 * Class which holds final static variables of operations that can work with just CalcModel.
 */
public class Operator {

    public static final IOperator SWAP_SIGN = CalcModel::swapSign;
    public static final IOperator CLEAR = CalcModel::clear;
    public static final IOperator RESET = CalcModel::clearAll;
    /* Apply stored BinaryOperator with activeOperand and currentValue of calculator, and after that display it */
    public static final IOperator EQUALS = model -> {
        double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
        model.clearAll();
        model.freezeValue(String.valueOf(result));
    };

}
