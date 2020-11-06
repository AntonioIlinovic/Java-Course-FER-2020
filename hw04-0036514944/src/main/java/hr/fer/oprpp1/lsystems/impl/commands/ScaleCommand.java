package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

public class ScaleCommand implements Command {

    double factor;

    public ScaleCommand(double factor) {
        this.factor = factor;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        double currentDrawLength = ctx.getCurrentState().getCurrentDrawLength();

        ctx.getCurrentState().setCurrentDrawLength(currentDrawLength * factor);
    }

}
