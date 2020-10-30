package hr.fer.oprpp1.custom.collections;

/**
 * Interface with only one method which calls method <code>process()</code> on given argument <code>value</code>.
 */
public interface Processor {

    /**
     * @param value to be processed.
     */
    void process(Object value);

}