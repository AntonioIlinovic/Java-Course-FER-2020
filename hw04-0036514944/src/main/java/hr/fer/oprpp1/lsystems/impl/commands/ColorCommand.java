package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.zemris.lsystems.Painter;

import java.awt.*;

/**
 * Accepts Color through constructor and sets it to the current TurtleState of the Context.
 */
public class ColorCommand implements Command {

    private Color color;

    public ColorCommand(Color color) {
        this.color = color;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setCurrentColor(color);
    }
}
