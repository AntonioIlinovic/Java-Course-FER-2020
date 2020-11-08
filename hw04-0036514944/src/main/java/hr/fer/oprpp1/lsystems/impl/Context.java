package hr.fer.oprpp1.lsystems.impl;

import hr.fer.oprpp1.collections.ObjectStack;

/**
 * This class instances enables the drawing of fractals. It has internal Stack which stores TurtleStates.
 */
public class Context {

    /* Internal Stack for storing TurtleStates */
    private ObjectStack<TurtleState> stack = new ObjectStack<>();

    /**
     * Returns TurtleState at the top of the Stack without removing it.
     * @return TurtleState at the top of the Stack
     */
    public TurtleState getCurrentState() {
        return stack.peek();
    }

    /**
     * Pushes new TurtleState at the top of the Stack.
     * @param state to push at the top of the Stack.
     */
    public void pushState(TurtleState state) {
        stack.push(state);
    }

    /**
     * Removes TurtleState at the top of the Stack.
     */
    public void popState() {
        stack.pop();
    }

}
