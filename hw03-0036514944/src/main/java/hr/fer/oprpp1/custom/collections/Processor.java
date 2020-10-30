package hr.fer.oprpp1.custom.collections;

/**
 * Interface with only one method which calls method <code>process()</code> on given argument <code>value</code>.
 * Parametrized by T.
 */
public interface Processor<T> {

    /**
     * @param value to be processed, of type T.
     */
    void process(T value);

}