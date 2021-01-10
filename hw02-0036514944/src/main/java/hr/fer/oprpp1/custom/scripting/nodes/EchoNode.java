package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * {@link Node} representing a command which generates some textual output dynamically.
 */
public class EchoNode extends Node {

    // Read-only variable
    private final Element[] elements;

    /**
     * Constructor for {@link EchoNode}.
     *
     * @param elements elements for {@link EchoNode}.
     */
    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    /**
     * Elements getter.
     *
     * @return Element array of {@link EchoNode}.
     */
    public Element[] getElements() {
        return elements;
    }

    @Override
    public void addChildNode(Node child) {
        throw new UnsupportedOperationException("EchoNode has no children");
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }

    @Override
    public Node getChild(int index) {
        throw new UnsupportedOperationException("EchoNode has no children");
    }

    /**
     * toString method for EchoNode. It prints in format such that if read again by a parser, it would result in the
     * same EchoNode structure.
     *
     * @return toString of a EchoNode
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{$ = ");
        for (Element element : elements) {
            stringBuilder.append(element.asText());
        }
        stringBuilder.append(" $}");

        return stringBuilder.toString();
    }
}
