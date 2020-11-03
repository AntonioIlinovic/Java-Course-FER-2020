package hr.fer.oprpp1.math;

/**
 * Class which represents vector with real components x and y.
 */
public class Vector2D {

    private double x;
    private double y;

    /**
     * Constructor for Vector2D.
     *
     * @param x real value on x-axis
     * @param y real value on y-axis
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x value.
     *
     * @return x real value on x-axis
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for y value.
     *
     * @return y real value on y-axis
     */
    public double getY() {
        return y;
    }

    /**
     * Method adds argument Vector2D to current Vector2D.
     *
     * @param offset Vector2D which is added to current Vector2D
     */
    public void add(Vector2D offset) {
        this.x += offset.getX();
        this.y += offset.getY();
    }

    /**
     * Method adds argument Vector2D and current Vector2D and returns result as new Vector2D. It doesn't change values
     * of current Vector2D object.
     *
     * @param offset Vector2D which is added to current Vector2D
     * @return new Vector2D as a result of adding current and argument Vector2D
     */
    public Vector2D added(Vector2D offset) {
        return new Vector2D(this.x + offset.getX(), this.y + offset.getY());
    }

    /**
     * Method rotates current Vector2D by given argument angle.
     *
     * @param angle angle to rotate current Vector2D object.
     */
    public void rotate(double angle) {
        double rx = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
        double ry = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
        this.x = rx;
        this.y = ry;
    }

    /**
     * Method returns new Vector2D by rotation of current Vector2D.
     *
     * @param angle angle to rotate current Vector2D object
     * @return new Vector2D created by rotation of current Vector2D by angle
     */
    public Vector2D rotated(double angle) {
        double rx = (this.x * Math.cos(angle)) - (this.y * Math.sin(angle));
        double ry = (this.x * Math.sin(angle)) + (this.y * Math.cos(angle));
        return new Vector2D(rx, ry);
    }

    /**
     * Method scales current Vector2D by given scalar.
     *
     * @param scaler scale current Vector2D by this scalar
     */
    public void scale(double scaler) {
        this.x *= scaler;
        this.y *= scaler;
    }

    /**
     * Method returns new Vector2D by scaling current Vector2D by given scalar.
     *
     * @param scaler scale current Vector2D by this scalar
     */
    public Vector2D scaled(double scaler) {
        return new Vector2D(this.x * scaler, this.y * scaler);
    }

    /**
     * Method returns new identical copy of current Vector2D.
     * @return identical copy of current Vector2D
     */
    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

}
