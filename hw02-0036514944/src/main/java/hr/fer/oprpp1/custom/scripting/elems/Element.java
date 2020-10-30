package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Used for the representation of expressions. Base class.
 */
public abstract class Element {

    /**
     * Returns String of each Element in such format that if parsed again it will be parsed equally.
     * @return Element in String format.
     */
    public abstract String asText();

}
