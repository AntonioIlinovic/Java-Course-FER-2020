package hr.fer.oprpp1.custom.collections;

/**
 * Represents some general parametrized collection of objects. Parametrized by T.
 */
public interface Collection<T> {

    /*
    Why collections of a type are not polymorphic on the type?
    If it were, next would be possible:

        // Create a List of Dog objects
        List<Dog> kennel = new ArrayList<Dog>();

        // Adding a Dog is no problem
        kennel.add( new Dog() );

        // The following line results in a compiler error
        List<Object> objs = kennel;

        // Because if it were allowed, we could do this
        objs.add( new Cat() );

        // And now we've got a Cat in our List of Dogs
     */

    /**
     * @return <code>true</code> if collection contains no objects,
     * <code>false</code> otherwise.
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * @return number of currently stored objects in this collection.
     */
    int size();

    /**
     * @param value added into the collection.
     */
    void add(T value);

    /**
     * @param value find if in collection.
     * @return boolean <code>true</code> if found, <code>false</code> otherwise.
     */
    boolean contains(Object value);

    /**
     * Remove object from collection.
     *
     * @param value to be removed.
     * @return boolean <code>true</code> if the collection contains given value as determined by <code>equals()</code>
     * method and removes one occurrence of it.
     */
    boolean remove(Object value);

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns
     * <code>null</code>.
     *
     * @return Collection converted to array of parametrized type T.
     */
    Object[] toArray();

    /**
     * Method calls <code>processor.process(.)</code> for each element of this {@link Collection}. The order in which
     * elements will be sent is defined by {@link ElementsGetter}.
     *
     * @param processor whose method <code>process()</code> is called on each element of this {@link Collection}.
     */
    default void forEach(Processor<? super T> processor) {
        // TODO Check this parametrization
        ElementsGetter<T> elementsGetter = createElementsGetter();
        while (elementsGetter.hasNextElement())
            processor.process(elementsGetter.getNextElement());
    }

    /**
     * Method adds into the current {@link Collection} all elements from the given collection. This other {@link
     * Collection} remains unchanged.
     *
     * @param other {@link Collection} from which elements are added to the current {@link Collection}.
     */
    default void addAll(Collection<? extends T> other) {
        // other.forEach((Processor<? super T>) value -> add(value));
        other.forEach((Processor<? super T>) this::add);
    }

    /**
     * Removes all elements from this collection.
     */
    void clear();

    /**
     * Object returns new ElementGetter which can fetch and return elements from Collection depending on
     * implementation.
     *
     * @return ElementsGetter object which is able to return Collection elements by calling its method.
     */
    ElementsGetter<T> createElementsGetter();

    /**
     * Add all elements from given {@link Collection} that satisfy {@link Tester} method to the current {@link
     * Collection}.
     *
     * @param col    from which elements are added to current {@link Collection}
     * @param tester check if iterated value is acceptable or not.
     */
    default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
        ElementsGetter<? extends T> elementsGetter = col.createElementsGetter();
        while (elementsGetter.hasNextElement()) {
            T nextElement = elementsGetter.getNextElement();
            if (tester.test(nextElement))
                add(nextElement);
        }
    }

}
