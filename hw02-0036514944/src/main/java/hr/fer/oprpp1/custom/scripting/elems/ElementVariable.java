package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only String property: name.
 */
public class ElementVariable extends Element {

    private final String name;

    /**
     * Constructor for {@link ElementVariable}.
     *
     * @param name String to initialize {@link ElementVariable} with
     */
    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * Getter for String name.
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Variable in String format
     */
    @Override
    public String asText() {
        return name + " ";
    }

}
