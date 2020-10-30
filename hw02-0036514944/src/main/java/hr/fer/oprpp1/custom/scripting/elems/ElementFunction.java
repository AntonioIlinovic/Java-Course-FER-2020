package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Inherits {@link Element}, and has a single read-only String property: name.
 */
public class ElementFunction extends Element {

    private final String name;

    /**
     * Constructor for {@link ElementFunction}.
     * @param name String to initialize {@link ElementFunction} with.
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * Getter for String name.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Function in String format.
     */
    @Override
    public String asText() {
        return "@" + name + " ";
    }

}
