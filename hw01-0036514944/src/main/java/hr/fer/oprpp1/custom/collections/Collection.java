package hr.fer.oprpp1.custom.collections;

/**
 * Represents some general collection of objects.
 */
public class Collection {

    protected Collection() {

    }

    /**
     * @return <code>true</code> if collection contains no objects and
     * <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * @return number of currently stored objects in this collection.
     */
    public int size() {
        // Implemented to always return 0.
        return 0;
    }

    /**
     * @param value added into the collection.
     */
    public void add(Object value) {
        // Implemented as empty method.
    }

    /**
     * @param value find if in collection.
     * @return boolean <code>true</code> if found, <code>false</code> otherwise.
     */
    public boolean contains(Object value) {
        // Implemented to always return false.
        return false;
        // It is OK to ask if collection contains null.
    }

    /**
     * Remove object from collection.
     *
     * @param value to be removed.
     * @return boolean <code>true</code> if the collection contains given value as determined by <code>equals()</code>
     * method and removes one occurrence of it.
     */
    public boolean remove(Object value) {
        // Implemented to always return false.
        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns
     * <code>null</code>.
     *
     * @return Collection converted to <code>Object</code> array.
     */
    public Object[] toArray() {
        // Implemented to throw UnsupportedOperationException.
        throw new UnsupportedOperationException();
    }

    /**
     * Method calls <code>processor.process(.)</code> for each element of this collection. The order in which elements
     * will be sent is undefined in this class.
     *
     * @param processor whose method <code>process()</code> is called on each element of this collection.
     */
    public void forEach(Processor processor) {
        // Implemented as an empty method.
    }

    /**
     * Method adds into the current collection all elements from the given collection. This other collection remains
     * unchanged.
     *
     * @param other Collection from which elements are added to the current collection.
     */
    public void addAll(Collection other) {

        /*
         * Implement it here to define a local processor class whose method process will
         * add each item into the current collection by calling method add, and then
         * call forEach on the other collection with this processor as argument. You
         * must define this new class directly in the method addAll.
         */
        class AddToCollectionProcessor extends Processor {

            public void process(Object value) {
                add(value);
            }

        }

        other.forEach(new AddToCollectionProcessor());
    }

    /**
     * Removes all elements from this collection.
     */
    public void clear() {
        // Implemented as an empty method.
    }

}
