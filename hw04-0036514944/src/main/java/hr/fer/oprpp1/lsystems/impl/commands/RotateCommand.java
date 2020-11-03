package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

public class RotateCommand implements Command {

    private double angle;

    public RotateCommand(double angle) {
        this.angle = angle;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().getCurrentAngle().rotate(angle);
    }

}
