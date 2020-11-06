package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.lsystems.impl.TurtleState;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;

public class SkipCommand implements Command {

    double step;

    public SkipCommand(double step) {
        this.step = step;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        TurtleState currentState = ctx.getCurrentState();
        Vector2D startPoint = currentState
                .getCurrentPosition();
        double drawLength = step * currentState.getCurrentDrawLength();
        Vector2D offset = currentState
                .getCurrentAngle()
                .scaled(drawLength);
        Vector2D endPoint = startPoint
                .added(offset);

        ctx.getCurrentState().setCurrentPosition(endPoint);
    }

}
