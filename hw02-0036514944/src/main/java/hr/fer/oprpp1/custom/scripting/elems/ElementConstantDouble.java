package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only double property: value.
 */
public class ElementConstantDouble extends Element {

    private final double value;

    /**
     * Constructor for {@link ElementConstantDouble}.
     *
     * @param value double to initialize {@link ElementConstantDouble} with
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Getter for double value.
     *
     * @return double value
     */
    public double getValue() {
        return value;
    }

    /**
     * @return ConstantDouble in String format
     */
    @Override
    public String asText() {
        return value + " ";
    }
}
