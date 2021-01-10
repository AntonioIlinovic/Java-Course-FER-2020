package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * {@link Node} representing an entire document.
 */
public class DocumentNode extends Node {

    // Node children of DocumentNode
    private ArrayIndexedCollection children;

    @Override
    public void addChildNode(Node child) {
        // If children is not initialized, initialize it
        if (children == null)
            children = new ArrayIndexedCollection();

        children.add(child);
    }

    @Override
    public int numberOfChildren() {
        return children.size();
    }

    @Override
    public Node getChild(int index) {
        Object result = children.get(index);
        if (result instanceof Node)
            return (Node) result;

        throw new RuntimeException("Child is not of type Node");
    }

    /**
     * toString method for DocumentNode. It prints in format such that if read again by a parser, it would result in the
     * same DocumentNode structure.
     *
     * @return toString of a DocumentNode
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // Iterate through DocumentNode children and add its toString to StringBuilder
        for (int noChild = 0; noChild < numberOfChildren(); noChild++)
            stringBuilder.append(
                    getChild(noChild).toString()
            );

        return stringBuilder.toString();
    }

    /**
     * Equals method for DocumentNode. It will convert DocumentNode to String format with toString method and test if
     * two Strings are equal.
     *
     * @param anotherNode to test if equal to current DocumentNode
     * @return <code>true</code> if equal, <code>false</code> otherwise
     */
    public boolean equals(DocumentNode anotherNode) {
        return this.toString().equals(anotherNode.toString());
    }
}
