package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

/**
 * Removes one TurtleState from the Context.
 */
public class PopCommand implements Command {

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.popState();
    }

}
