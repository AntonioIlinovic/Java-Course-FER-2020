package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

/**
 * Functional Interface with method execute.
 */
public interface Command {

    /**
     * Executes Command instance using given Context and Painter.
     * @param ctx Context
     * @param painter Painter
     */
    void execute(Context ctx, Painter painter);

}
