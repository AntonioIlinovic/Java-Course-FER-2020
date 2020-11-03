package hr.fer.oprpp1.collections;

/**
 * One of RuntimeException which is thrown when there is a problem with empty Stack.
 */
public class EmptyStackException extends RuntimeException {

    public EmptyStackException(String errorMessage) {
        super(errorMessage);
    }

}
