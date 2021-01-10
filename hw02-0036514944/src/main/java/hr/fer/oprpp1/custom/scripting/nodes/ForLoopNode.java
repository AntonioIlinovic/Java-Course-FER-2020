package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * {@link Node} representing a single for-loop construct.
 */
public class ForLoopNode extends Node {

    // Read-only variables
    private final ElementVariable variable;
    private final Element startExpression;
    private final Element endExpression;
    private final Element stepExpression;       // Can be null

    // children Collection
    private ArrayIndexedCollection children;

    /**
     * Constructor for ForLoopNode. First 3 arguments are required, 4th one is optional.
     *
     * @param variable        variable of for loop
     * @param startExpression start expression of for loop
     * @param endExpression   end expression of for loop
     * @param stepExpression  step expression of for loop
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        if (variable == null || startExpression == null || endExpression == null)
            throw new SmartScriptParserException("First 3 arguments of for loop can't be null");

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public void addChildNode(Node child) {
        // If children Collection is not initialized, initialize it
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
     * toString method for ForLoopNode. It prints in format such that if read again by a parser, it would result in the
     * same ForLoopNode structure.
     *
     * @return toString of a ForLoopNode.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{$ FOR ")
                .append(variable.asText())
                .append(startExpression.asText())
                .append(endExpression.asText());
        if (stepExpression != null)
            stringBuilder.append(stepExpression.asText());
        stringBuilder.append(" $}");

        // Add toString of for loop children to StringBuilder
        for (int noChild = 0; noChild < numberOfChildren(); noChild++) {
            Node childNode = getChild(noChild);
            stringBuilder.append(childNode.toString());
        }

        // Add close tag to StringBuilder
        stringBuilder.append("{$ END $}");

        return stringBuilder.toString();
    }
}
