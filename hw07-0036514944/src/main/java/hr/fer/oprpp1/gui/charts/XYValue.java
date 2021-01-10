package hr.fer.oprpp1.gui.charts;

/**
 * Razred XYValue koji ima dva read-only propertyja: x i y, oba tipa int.
 */
public class XYValue {

    private final int x;
    private final int y;

    /**
     * Konstruktor za XYValue, prima dva int argumenta, x i y vrijednost.
     * @param x vrijednost
     * @param y vrijednost
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
