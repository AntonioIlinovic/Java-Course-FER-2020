package hr.fer.oprpp1.custom.collections;

/**
 * Stack-like collection of type T. The collection ArrayIndexedCollection will be used for implementation of ObjectStack
 * class. Design pattern Adapter is used to give user methods which are expected for stack (push, pop, peek) and not
 * (insert, add).
 */
public class ObjectStack<T> {

    // This implementation holds one private ArrayIndexedCollection which is used as a stack
    private final ArrayIndexedCollection<T> stack;

    /**
     * Default {@link ObjectStack} constructor.
     */
    public ObjectStack() {
        stack = new ArrayIndexedCollection<T>();
    }

    /**
     * Method returns if ObjectStack is empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * @return number of elements in the stack.
     */
    public int size() {
        return stack.size();
    }

    /**
     * Pushes given value on the stack. Null value is not allowed.
     *
     * @param value to be pushed on stack.
     */
    public void push(T value) {
        if (value == null) throw new IllegalArgumentException("Value to be pushed on stack must not be null.");

        // Implemented with o(1) complexity
        stack.add(value);
    }

    /**
     * Removes last value pushed on stack from stack and returns it. If stack is empty when method pop() is called,
     * {@link EmptyStackException} is thrown.
     *
     * @return last Object pushed on stack
     */
    public T pop() {
        if (isEmpty()) throw new EmptyStackException("You can not call pop() when stack is empty.");

        // Implemented in o(1) complexity
        T last = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);

        return last;
    }

    /**
     * Returns last element placed on stack but does not delete it from stack. If stack is empty when method peek() is
     * called, {@link EmptyStackException} is thrown.
     *
     * @return last Object pushed on stack
     */
    public T peek() {
        if (isEmpty()) throw new EmptyStackException("You can not call peek() when stack is empty.");

        return stack.get(stack.size() - 1);
    }

    /**
     * Removes all elements from stack.
     */
    public void clear() {
        stack.clear();
    }

}
