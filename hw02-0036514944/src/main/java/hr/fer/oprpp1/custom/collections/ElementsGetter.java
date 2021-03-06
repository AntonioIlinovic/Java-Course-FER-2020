package hr.fer.oprpp1.custom.collections;

/**
 * Interface which can fetch and return elements from Collection depending on implementation. If Collection is changed while
 * {@link ElementsGetter} is in usage, throw {@link java.util.ConcurrentModificationException}.
 */
public interface ElementsGetter {

    /**
     * @return boolean <code>true</code> if has next element in Collection, <code>false</code> otherwise.
     */
    boolean hasNextElement();

    /**
     * @return Object which is next in the Collection.
     */
    Object getNextElement();

    /**
     * Iterate remaining elements and process it with Processor object.
     *
     * @param p Processor which will process remaining elements.
     */
    default void processRemaining(Processor p) {
        while (hasNextElement())
            p.process(getNextElement());
    }

}
