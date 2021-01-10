package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only int property: value.
 */
public class ElementConstantInteger extends Element {

    private final int value;

    /**
     * Constructor for {@link ElementConstantInteger}.
     *
     * @param value int to initialize {@link ElementConstantInteger} with
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Getter for int value.
     *
     * @return int value
     */
    public int getValue() {
        return value;
    }

    /**
     * @return ConstantInteger in String format
     */
    @Override
    public String asText() {
        return value + " ";
    }
}
