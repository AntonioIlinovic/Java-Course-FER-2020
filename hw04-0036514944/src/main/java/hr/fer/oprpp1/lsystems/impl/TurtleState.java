package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;

/**
 * Class which remembers current state of the turtle. It has its position, angle, color, and current draw length.
 */
public class TurtleState {

    /* Current turtle's Position as radius-vector */
    private Vector2D currentPosition;
    /* Current turtle's Position as direction vector */
    private Vector2D currentAngle;
    /* Current color by which turtle draws */
    private Color currentColor;
    /* Current effective draw length which corresponds if it gets command to draw a unit length */
    private double currentDrawLength;

    /**
     * Constructor for TurtleState saves given state.
     * @param currentPosition currentPosition
     * @param currentAngle currentAngle
     * @param currentColor currentColor
     * @param currentDrawLength currentDrawLength
     */
    public TurtleState(Vector2D currentPosition, Vector2D currentAngle, Color currentColor, double currentDrawLength) {
        this.currentPosition = currentPosition;
        this.currentAngle = currentAngle;
        this.currentColor = currentColor;
        this.currentDrawLength = currentDrawLength;
    }

    public Vector2D getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Vector2D currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Vector2D getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(Vector2D currentAngle) {
        this.currentAngle = currentAngle;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public double getCurrentDrawLength() {
        return currentDrawLength;
    }

    public void setCurrentDrawLength(double currentDrawLength) {
        this.currentDrawLength = currentDrawLength;
    }

    /**
     * Returns a new Instance of this TurtleState.
     * @return a new Instance of this TurtleState
     */
    public TurtleState copy() {
        return new TurtleState(currentPosition, currentAngle, currentColor, currentDrawLength);
    }

    @Override
    public String toString() {
        return "TurtleState{" +
                "currentPosition=" + currentPosition +
                ", currentAngle=" + currentAngle +
                ", currentColor=" + currentColor +
                ", currentDrawLength=" + currentDrawLength +
                '}';
    }
}
