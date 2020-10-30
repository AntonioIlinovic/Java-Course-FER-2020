package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * {@link Node} representing a piece of textual data.
 */
public class TextNode extends Node {

    // Read-only variable
    private final String text;

    /**
     * Constructor for {@link TextNode}.
     * @param text text for {@link TextNode}.
     */
    public TextNode(String text) {
        this.text = text;
    }

    /**
     * Text getter.
     * @return text of {@link TextNode}.
     */
    public String getText() {
        return text;
    }

    @Override
    public void addChildNode(Node child) {
        throw new UnsupportedOperationException("TextNode has no children");
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }

    @Override
    public Node getChild(int index) {
        throw new UnsupportedOperationException("TextNode has no children");
    }

    /**
     * toString method for TextNode. It prints in format such that if read again by a parser, it would result
     * in the same TextNode structure.
     * @return toString of a TextNode.
     */
    @Override
    public String toString() {
        return text;
    }
}
