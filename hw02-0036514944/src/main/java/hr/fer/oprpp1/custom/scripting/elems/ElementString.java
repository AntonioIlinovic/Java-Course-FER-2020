package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only property: value.
 */
public class ElementString extends Element {

    private final String value;

    /**
     * Constructor for {@link ElementString}.
     *
     * @param value String to initialize {@link ElementString} with
     */
    public ElementString(String value) {
        this.value = value;
    }

    /**
     * Getter for String value.
     *
     * @return String value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return String in String format
     */
    @Override
    public String asText() {
        return "\"" + value + "\" ";
    }
}
