package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

/**
 * Current state of the Context is copied and pushed to the top of the Context.
 */
public class PushCommand implements Command {

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.pushState(ctx.getCurrentState().copy());
    }

}
