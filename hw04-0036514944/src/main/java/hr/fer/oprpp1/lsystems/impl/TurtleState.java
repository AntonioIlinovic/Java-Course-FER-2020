package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;

public class TurtleState {

    private Vector2D currentPosition;
    private Vector2D currentAngle;
    private Color currentColor;
    private double currentDrawLength;

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
