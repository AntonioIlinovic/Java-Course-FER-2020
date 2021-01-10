package hr.fer.oprpp1.gui.calc;

import hr.fer.oprpp1.gui.calc.model.CalcModel;

/**
 * Interface like the Function interface, with only one method apply.
 */
public interface IOperator {

    /**
     * Operator applied to the given model.
     *
     * @param model
     */
    public void apply(CalcModel model);

}
