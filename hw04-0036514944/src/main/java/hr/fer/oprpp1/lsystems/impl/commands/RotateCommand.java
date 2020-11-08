package hr.fer.oprpp1.lsystems.impl.commands;

import hr.fer.oprpp1.lsystems.impl.Context;
import hr.fer.oprpp1.math.AngleConverter;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;

import static hr.fer.oprpp1.math.AngleConverter.RadiansToDegrees;

/**
 * Accepts angle through constructor. Changes current TurtleState from the Context so it has that angle.
 */
public class RotateCommand implements Command {

    private double angle;

    // TODO Delete this. This is a method used if you want more randomness in your fractal
    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public RotateCommand(double angle) {
        //this.angle = angle + getRandomNumber(-10, 10); // This is for more randomness in your fractal
        this.angle = angle;
    }

    @Override
    public void execute(Context ctx, Painter painter) {
        Vector2D currentAngle = ctx.getCurrentState().getCurrentAngle();
        Vector2D newAngle = currentAngle.rotated(AngleConverter.DegreesToRadians(angle));

        ctx.getCurrentState().setCurrentAngle(newAngle);
    }

}
