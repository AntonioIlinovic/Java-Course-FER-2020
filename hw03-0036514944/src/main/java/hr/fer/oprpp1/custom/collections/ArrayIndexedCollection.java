package hr.fer.oprpp1.custom.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Implementation of resizable array-backed collection of objects. Duplicate elements <b>are allowed</b>, storage of
 * null references
 * <b>is not allowed</b>.
 * Parametrized by T.
 */
public class ArrayIndexedCollection<T> implements List<T> {

    private int size;
    private T[] elements;
    private final static int DEFAULT_CAPACITY = 16;
    /*
     Counts how many times this Collection structure has been modified.
     In ArrayIndexedCollection reallocating array or shifting elements right or left will increment this value.
     */
    private long modificationCount = 0;

    /**
     * Default constructor. Size of array is set to default value.
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * {@link ArrayIndexedCollection} constructor with one parameter InitialCapacity.
     *
     * @param initialCapacity how much space is allocated for storage.
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException(
                    "Initial Capacity must be 1 or more. You tried to set it to: " + initialCapacity);

        size = 0;
        elements = (T[]) new Object[initialCapacity];
    }

    /**
     * Constructor which accepts reference to other <code>Collection</code> which elements are copied into this newly
     * constructed <code>Collection</code>.
     *
     * @param other Collection whose elements are copied to the current Collection.
     */
    public ArrayIndexedCollection(Collection<? extends T> other) {
        this(other, DEFAULT_CAPACITY);
    }

    /**
     * Constructor which accepts reference to other <code>Collection</code> which elements are copied into this newly
     * constructed <code>Collection</code>.
     *
     * @param other           Collection whose elements are copied to the current Collection.
     * @param initialCapacity how much space is allocated for storage.
     */
    @SuppressWarnings("unchecked")
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
        if (other == null)
            throw new NullPointerException("Given Collection reference must not be null.");

        /*
         * If the initialCapacity is smaller than the size of the given collection, the
         * size of the given collection should be used for elements array allocation.
         */
        int biggerInitialCapacity = initialCapacity;
        if (initialCapacity < other.size()) {
            biggerInitialCapacity = other.size();
        }

        elements = (T[]) new Object[biggerInitialCapacity];

        this.addAll(other);
    }

    /**
     * Returns number of allocated places for elements.
     * @return number of allocated places for elements
     */
    public int getAllocatedSize() {
        return elements.length;
    }

    /**
     * @return number of currently stored objects in this Collection.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object into current Collection in the first empty space.
     *
     * @param value to be added into current Collection.
     */
    @Override
    public void add(T value) {
        /*
         * The method should refuse to add null as element by throwing
         * NullPointerException. Average complexity of this method is log2(n).
         */
        if (value == null)
            throw new NullPointerException("Storing null references is not allowed.");

        /*
         * Reference value is added into first empty place in the elements array. If the
         * elements array is full, it should be reallocated by doubling its size.
         */
        if (size < elements.length) {
            elements[size++] = value;
            return;
        }

        // Reallocate elements and increase capacity of container.
        @SuppressWarnings("unchecked")
        T[] reallocatedElements = (T[]) new Object[2 * elements.length];
        System.arraycopy(elements, 0, reallocatedElements, 0, size);
        reallocatedElements[size++] = value;
        modificationCount++;
        elements = reallocatedElements;
    }

    /**
     * Returns <code>true</code> only if the collection contains given value, as determined by <code>equals()</code>
     * method.
     *
     * @param value to find if contained in Collection.
     * @return boolean
     */
    @Override
    public boolean contains(Object value) {
        for (int index = 0; index < size; index++) {
            if (elements[index].equals(value))
                return true;
        }

        return false;
    }

    /**
     * Allocates new array with size equals to the size of this collections, fills it with collection content and
     * returns the array. This method never returns
     * <code>null</code>.
     *
     * @return Object array of collection elements.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];

        if (size >= 0) {
            System.arraycopy((Object[])elements, 0, array, 0, size);
        }

        return array;
    }

    /**
     * Removes all elements from the collection.
     */
    @Override
    public void clear() {
        /*
         * The allocated array is left at current capacity. Size is not just set to 0,
         * instead null references are written into the backing array so that object so
         * that objects which became unreferenced become eligible for garbage
         * collection. New array is not allocated.
         */
        for (int index = 0; index < size; index++) {
            elements[index] = null;
        }
        size = 0;
        modificationCount++;
    }

    /**
     * Returns the object that is stored in backing array at position index.
     *
     * @param index from where to return the value.
     * @return value at position index.
     */
    public T get(int index) {
        /*
         * Valid indexes are 0 to size-1. If index is invalid, IndexOutOfBoundsException
         * is thrown. Average complexity of this method is o(1).
         */
        if (index < 0 || index > size - 1)
            throw new IndexOutOfBoundsException(
                    "Index must be between 0 and " + (size - 1) + ". Yours index value was " + index + ".");

        return elements[index];
    }

    /**
     * Inserts (does not overwrite) the given value at the given position in array. Elements at position and higher are
     * shifted one place towards the end.
     *
     * @param value    to be inserted.
     * @param position where to insert.
     */
    public void insert(T value, int position) {
        if (value == null)
            throw new IllegalArgumentException("Storing null references is not allowed.");

        /*
         * Legal positions are 0 to size. If position is not valid,
         * IndexOutOfBoundsException is thrown.
         */
        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException(
                    "Position value must be between 0 and " + size + ". Yours was " + position + ".");

        /*
         * First we add last element again so it is shifted one place towards the end.
         * If needed this will increase the capacity of the array. After that we shift
         * all other elements from second-to-last to the element at position. And then
         * we just insert the value at the now shifted position.
         */
        add(elements[size - 1]);
        for (int index = size - 2; index >= position; index--) {
            elements[index + 1] = elements[index];
            modificationCount++;
        }

        elements[position] = value;
    }

    /**
     * Searches the collection and returns the index of the first occurrence of the given value or <code>-1</code> if
     * the value is not found. If argument is
     * <code>null</code>, result will be that this element is not found. Average
     * complexity of this method is n.
     *
     * @param value to find the index of.
     * @return index if value is found, -1 otherwise.
     */
    public int indexOf(Object value) {
        for (int index = 0; index < size; index++) {
            if (elements[index].equals(value))
                return index;
        }

        return -1;
    }

    /**
     * Returns <code>true</code> only if the Collection contains given value as determined by <code>equals()</code>
     * method and removes one occurrence of it.
     *
     * @param value to be removed from Collection.
     * @return boolean <code>true</code> if object is removed from Collection, <code>false</code> otherwise.
     */
    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        if (index == -1)
            return false;
        remove(index);

        return true;
    }

    /**
     * Removes element at specified index from collection. Element that was previously at location index+1 after this
     * operation is on location index, etc.
     *
     * @param index from which element is removed.
     */
    public void remove(int index) {
        /*
         * Legal indexes are 0 to size-1. In case of invalid index
         * IndexOutOfBoundsException is thrown.
         */
        if (index < 0 || index > size - 1)
            throw new IndexOutOfBoundsException(
                    "Index must be between 0 and " + (size - 1) + ". Yours was " + index + ".");

        if (index != size -1) {
            for (index = index + 1; index <= size; index++) {
                elements[index - 1] = elements[index];
            }
            index--;
        }
        elements[index] = null;

        size--;
    }

    /**
     * @return new ElementGetter which can fetch and return Collection elements.
     */
    @Override
    public ElementsGetter<T> createElementsGetter() {
        /*
        We send reference of current Collection so private static class can "see" variables of non-static class.
         */
        return new ArrayIndexedCollectionElementsGetter<T>(this, modificationCount);
    }

    /**
     * Implementation of {@link ElementsGetter} for {@link ArrayIndexedCollection}.
     */
    private static class ArrayIndexedCollectionElementsGetter<T> implements ElementsGetter<T> {

        private final ArrayIndexedCollection<T> collectionReference;
        private int currentElementIndex = 0;
        private final long savedModificationCount;

        public ArrayIndexedCollectionElementsGetter(ArrayIndexedCollection<T> collectionReference, long savedModificationCount) {
            this.collectionReference = collectionReference;
            this.savedModificationCount = savedModificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (savedModificationCount != collectionReference.modificationCount)
                throw new ConcurrentModificationException("Collection changed while ElementsGetter is in use.");
            return currentElementIndex < collectionReference.size;
        }

        @Override
        public T getNextElement() {
            if (hasNextElement())
                return collectionReference.get(currentElementIndex++);
            throw new NoSuchElementException("No more elements in Collection ElementsGetter.");
        }

    }

    @Override
    public String toString() {
        return "ArrayIndexedCollection{" +
                "size=" + size +
                ", elements=" + Arrays.toString(elements) +
                '}';
    }
}
