package hr.fer.oprpp1.custom.collections;

/**
 * Represents some general collection of objects.
 */
public interface Collection {

    /**
     * Returns <code>true</code> if collection contains no objects, <code>false</code> otherwise.
     *
     * @return <code>true</code> if collection contains no objects, <code>false</code> otherwise
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Returns number of currently stored objects in this collection.
     *
     * @return number of currently stored objects in this collection
     */
    int size();

    /**
     * Adds given value into the collection.
     *
     * @param value added into the collection
     */
    void add(Object value);

    /**
     * Checks if given value is in the collection.
     *
     * @param value to find if in collection
     * @return boolean <code>true</code> if found, <code>false</code> otherwise
     */
    boolean contains(Object value);

    /**
     * Removes object from collection and returns if it removed one occurrence of given object.
     *
     * @param value to be removed.
     * @return boolean <code>true</code> if the collection contains given value as determined by <code>equals()</code>
     * method and removes one occurrence of it
     */
    boolean remove(Object value);

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns <code>null</code>.
     *
     * @return Collection converted to <code>Object</code> array
     */
    Object[] toArray();

    /**
     * Method calls <code>processor.process(.)</code> for each element of this {@link Collection}. The order in which
     * elements will be sent is defined by {@link ElementsGetter}.
     *
     * @param processor whose method <code>process()</code> is called on each element of this {@link Collection}.
     */
    default void forEach(Processor processor) {
        ElementsGetter elementsGetter = createElementsGetter();

        while (elementsGetter.hasNextElement())
            processor.process(elementsGetter.getNextElement());
    }

    /**
     * Method adds into the current {@link Collection} all elements from the given collection. This other {@link
     * Collection} remains unchanged.
     *
     * @param other {@link Collection} from which elements are added to the current {@link Collection}
     */
    default void addAll(Collection other) {
        other.forEach(new Processor() {
            @Override
            public void process(Object value) {
                add(value);
            }
        });
    }

    /**
     * Removes all elements from this collection.
     */
    void clear();

    /**
     * Object returns new ElementGetter which can fetch and return elements from Collection depending on
     * implementation.
     *
     * @return ElementsGetter object which is able to return Collection elements by calling its method
     */
    ElementsGetter createElementsGetter();

    /**
     * Add all elements from given {@link Collection} that satisfy {@link Tester} method to the current {@link
     * Collection}.
     *
     * @param col    from which elements are added to current {@link Collection}
     * @param tester check if iterated value is acceptable or not
     */
    default void addAllSatisfying(Collection col, Tester tester) {
        ElementsGetter elementsGetter = col.createElementsGetter();

        while (elementsGetter.hasNextElement()) {
            Object nextElement = elementsGetter.getNextElement();
            if (tester.test(nextElement))
                add(nextElement);
        }
    }

}
