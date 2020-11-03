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
                .scaled(step);
        Vector2D endPoint = currentState
                .getCurrentPosition()
                .added(offset);

        TurtleState newState = new TurtleState(
                endPoint,
                currentState.getCurrentAngle(),
                currentState.getCurrentColor(),
                currentState.getCurrentDrawLength()
        );
        ctx.pushState(newState);
    }

}
