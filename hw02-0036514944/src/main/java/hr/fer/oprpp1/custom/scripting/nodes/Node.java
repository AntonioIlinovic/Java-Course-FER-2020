package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Used for representation of structured documents. Base class for all graph nodes. Base element for syntax tree which
 * is built by {@link hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser}. Node can have a {@link
 * hr.fer.oprpp1.custom.collections.Collection} of children which are also {@link Node}.
 */
public abstract class Node {

    /**
     * Adds the given child to an internally managed {@link hr.fer.oprpp1.custom.collections.Collection} of children. An
     * instance of {@link hr.fer.oprpp1.custom.collections.ArrayIndexedCollection} is used for this. However, that
     * Collection is created only when actually needed. (i.e. create an instance of the Collection on demand -> on first
     * call of addChildNode).
     *
     * @param child Node added to internally managed Collection of children
     */
    public abstract void addChildNode(Node child);

    /**
     * Returns a number of (direct) children.
     *
     * @return number of (direct) children
     */
    public abstract int numberOfChildren();

    /**
     * Returns selected child or throws an appropriate exception if the index is invalid.
     *
     * @param index of child to select
     * @return selected child
     */
    public abstract Node getChild(int index);

    /**
     * toString method for Node. It prints in format such that if read again by a parser, it would result in the same
     * Node structure.
     *
     * @return toString of a Node
     */
    @Override
    public abstract String toString();

}
