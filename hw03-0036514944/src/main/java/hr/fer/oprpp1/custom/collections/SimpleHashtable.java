package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class which implements a Map. It is parametrized by key with type K, and value with type V. Class represents hash
 * table which enables storage of key-value pairs. It has internal array of references to head of LinkedList. Node of
 * LinkedList is modeled with TableEntry class.
 *
 * @param <K> type of key
 * @param <V> type of value
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

    private int size;                   // Number of TableEntries in HashTable
    private TableEntry<K, V>[] table;   // Table of Entries
    private int slots;                  // Number of allocated slots
    private int modificationCount;      // Initialized when instance is created, used to throw error when changing while iterating
    private final static int DEFAULT_CAPACITY = 16;

    /**
     * Default constructor for SimpleHashtable. It will initialize an empty array of 16 slots for TableEntries.
     */
    public SimpleHashtable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor for SimpleHashtable. It will initialize an empty array of first power of 2 that is greater or equal
     * to argument capacity (i.e. if you give capacity = 20, 32 is chosen. If you give capacity = 63, 64 is chosen) that
     * are slots for TableEntries.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("SimpleHashtable constructor accepts only initialCapacity more or equal to 1, yours was: " + capacity);

        this.size = 0;
        this.modificationCount = 0;
        // This sets initialCapacity to first power of 2 greater or equal to argument initialCapacity
        capacity = (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(capacity - 1));

        table = (TableEntry<K, V>[]) new TableEntry[capacity];
        this.slots = capacity;
    }

    /**
     * Getter for number of slots.
     *
     * @return number of slots allocated in memory
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Updates or adds new TableEntry to Hashtable depending if there was TableEntry with given key. If Hashtable
     * fill-ratio (num_entries / num_slots) becomes >= 75%, number of slots is doubled, and all elements are
     * reallocated.
     *
     * @param key   key for new TableEntry
     * @param value value for new TableEntry
     * @return null if there was no TableEntry with given key, value of overwritten TableEntry otherwise
     */
    public V put(K key, V value) {
        if (key == null)
            throw new NullPointerException("Hashtable key can't be null");

        // Reallocation if fill-ratio >= 75%
        if (((double) size / (double) slots) >= 0.75)
            reallocateTable();

        int slotIndex = calculateSlot(key);
        if (table[slotIndex] == null) {
            // If there is no entries in given slot
            table[slotIndex] = new TableEntry<>(key, value, null);
            size++;
            modificationCount++;
            return null;
        }

        TableEntry<K, V> tableWalker = table[slotIndex];
        // Move tableWalker until you get to the entry with the same key or end of table slot
        while (tableWalker.next != null && !tableWalker.getKey().equals(key))
            tableWalker = tableWalker.next;

        if (tableWalker.getKey().equals(key)) {
            // TableEntry is updated, modificationCount is not incremented
            V oldValue = tableWalker.getValue();
            tableWalker.setValue(value);
            return oldValue;
        } else {
            // tableWalker is at the last entry and there is no entry with given key
            tableWalker.next = new TableEntry<>(key, value, null);
            size++;
            modificationCount++;
            return null;
        }
    }

    /**
     * Reallocates and doubles table of TableEntry.
     */
    @SuppressWarnings("unchecked")
    private void reallocateTable() {
        modificationCount++;
        TableEntry<K, V>[] tableEntries = toArray();
        /* clear() sets size to 0. Size is incremented while reallocating all TableEntries,
        method reallocateTable() won't be called while reallocating */
        clear();
        slots *= 2;                             // double the number of slots in Hashtable
        table = (TableEntry<K, V>[]) new TableEntry[slots];

        // We can't just use System.arraycopy(...), because number of slots is changed, therefore calculated indexes of TableEntries are also changed
        for (TableEntry<K, V> entry : tableEntries)
            put(entry.getKey(), entry.getValue());
    }

    /**
     * Clears all TableEntries from Hashtable. It doesn't change capacity (slots) of Hashtable.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        table = (TableEntry<K, V>[]) new TableEntry[slots];
        modificationCount++;
        size = 0;
    }

    /**
     * Returns TableEntry with given key if Hashtable has it, null otherwise.
     *
     * @param key check if Hashtable contains TableEntry with given key
     * @return value of TableEntry with given key if it exists, null otherwise
     */
    public V get(Object key) {
        if (key == null)
            return null;
        int slotIndex = calculateSlot(key);
        if (table[slotIndex] == null)
            return null;

        TableEntry<K, V> tableWalker = table[slotIndex];
        // Move tableWalker until you get to the entry with the same key or end of table slot
        while (tableWalker.next != null && !tableWalker.getKey().equals(key))
            tableWalker = tableWalker.next;

        return tableWalker.getKey().equals(key) ? tableWalker.getValue() : null;
    }

    /**
     * Returns number of TableEntries in HashTable.
     *
     * @return number of TableEntries in HashTable
     */
    public int size() {
        return size;
    }

    /**
     * Checks if Hashtable contains TableEntry with given key.
     *
     * @param key check if Hashtable contains TableEntry with given key
     * @return <code>true</code> if it contains, <code>false</code> otherwise
     */
    public boolean containsKey(Object key) {
        if (key == null)
            return false;
        int slotIndex = calculateSlot(key);
        if (table[slotIndex] == null)
            return false;

        /* We could implement it using iterator, but this will jump straight to slot of a key, therefore it's more efficient */
        TableEntry<K, V> tableWalker = table[slotIndex];
        // Move tableWalker until you get to the entry with the same key or end of table slot
        while (tableWalker.next != null && !tableWalker.getKey().equals(key))
            tableWalker = tableWalker.next;

        return tableWalker.getKey().equals(key);
    }

    /**
     * Checks if Hashtable contains TableEntry with given value.
     *
     * @param value check if Hashtable contains TableEntry with given value
     * @return <code>true</code> if it contains, <code>false</code> otherwise
     */
    public boolean containsValue(Object value) {
        for (TableEntry<K, V> entry : this)
            if (entry.getValue().equals(value))
                return true;

        return false;
    }

    /**
     * Removes from table TableEntry with given key, if that entry exists returns caller value from removed element.
     *
     * @param key remove element with this key
     * @return value of removed element, or <code>null</code> if nothing was removed
     */
    public V remove(Object key) {
        if (key == null)
            return null;
        if (!containsKey(key))
            return null;

        int slotIndex = calculateSlot(key);
        if (table[slotIndex] == null)
            return null;

        TableEntry<K, V> tableWalker = table[slotIndex];
        V oldValue;
        // Change modificationCount and size in one place because we are sure to find an Entry with this key (we called containsKey())
        modificationCount++;
        size--;

        // If Entry with given key is first in the slot
        if (tableWalker.getKey().equals(key)) {
            oldValue = tableWalker.getValue();
            table[slotIndex] = tableWalker.next;
            return oldValue;
        }

        /* Until we find Entry within slot. We are sure to find an Entry with this key, because we called containsKey().
        tableWalker will be positioned one element before key that needs to be removed so we can change next pointers correctly. */
        while (!tableWalker.next.getKey().equals(key))
            tableWalker = tableWalker.next;

        oldValue = tableWalker.next.getValue();     // Stores value of removed Entry
        tableWalker.next = tableWalker.next.next;   // Pointer of element before removed is redirected to that after removed
        return oldValue;
    }

    /**
     * Returns true if HashTable is empty, false otherwise.
     *
     * @return true if HashTable is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns elements stored in Hashtable in String format: [key1=value1, key2=value2, key3=value3]
     *
     * @return Hashtable elements as a String in format: [key1=value1, key2=value2, key3=value3]
     */
    @Override
    public String toString() {
        Iterator<TableEntry<K, V>> elementsGetter = iterator();

        StringBuilder sb = new StringBuilder("[");
        while (elementsGetter.hasNext()) {
            sb.append(elementsGetter.next().toString());
            if (elementsGetter.hasNext())
                sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    /**
     * Allocates new array with size equals to the size of this Hashtable, fills it with Hashtable content and returns
     * the array.
     *
     * @return TableEntry array of Hashtable elements
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        Iterator<TableEntry<K, V>> iterator = iterator();
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];
        int index = 0;

        // Fills newly created array with Entries
        while (iterator.hasNext())
            array[index++] = iterator.next();

        return array;
    }

    /**
     * Helper method which calculates in what slot should key be inserted.
     *
     * @param key to calculate slot index
     * @return slot index to insert key
     */
    private int calculateSlot(Object key) {
        return Math.abs(key.hashCode()) % slots;
    }

    /**
     * Helper public method (adding or removing entries from hashtable while iterating without using iterators method
     * may throw an exception) which fetches and returns entries from hashtable.
     *
     * @return new Iterator which can fetch and return SimpleHashTable TableEntries
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

    /**
     * Implementation of Iterator for SimpleHashtable.
     */
    private class IteratorImpl implements Iterator<TableEntry<K, V>> {

        private int currentElementNo;               // Element no.
        private int currentSlotIndex;               // Slot no.
        private TableEntry<K, V> slotWalker;        // Current Element
        private boolean nextCalled;                 // Used in remove, next must be called before calling remove
        private K lastKey;                          // Key of last iterated element
        private int savedModificationCount;         // Initialized when instance is created, used to throw error when changing while iterating

        public IteratorImpl() {
            this.currentElementNo = 1;
            this.currentSlotIndex = 0;
            this.nextCalled = false;
            this.lastKey = null;
            savedModificationCount = SimpleHashtable.this.modificationCount;
        }

        /**
         * Returns true if the iteration has more elements. (In other words, returns true if next() would return an
         * element rather than throwing an exception.)
         *
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            if (savedModificationCount != SimpleHashtable.this.modificationCount)
                throw new ConcurrentModificationException("Hashtable modified while iterating");

            return currentElementNo <= size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         */
        @Override
        public TableEntry<K, V> next() {
            if (savedModificationCount != SimpleHashtable.this.modificationCount)
                throw new ConcurrentModificationException("Hashtable modified while iterating");
            if (!hasNext())
                throw new NoSuchElementException("No more elements in Collection ElementsGetter.");

            nextCalled = true;

            /* At start or when at the end of slot, go to next slot. We don't need to check if currentSlotIndex
            will exceed number of slots in the table because method hasNext() returned true. */
            while (slotWalker == null) {
                slotWalker = table[currentSlotIndex];
                currentSlotIndex++;
            }

            // Move walker to next entry in slot
            TableEntry<K, V> nextTableEntry = slotWalker;
            slotWalker = slotWalker.next;
            lastKey = nextTableEntry.getKey();
            currentElementNo++;
            return nextTableEntry;
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator (optional operation). This
         * method can be called only once per call to next(). The behavior of an iterator is unspecified if the
         * underlying collection is modified while the iteration is in progress in any way other than by calling this
         * method.
         */
        @Override
        public void remove() {
            if (savedModificationCount != SimpleHashtable.this.modificationCount)
                throw new ConcurrentModificationException("Hashtable modified while iterating");
            if (!nextCalled)
                throw new IllegalStateException("Can't call remove method in iterator if the next method hasn't yet been called," +
                        " or the remove method has already been called after the last call to the next");

            nextCalled = false;

            /* If there is an entry with lastKey that iterator found in next method, this iterator
            will increase its savedModificationCount and remove entry with lastKey. This is so iterator
            removes entry with control and doesn't throw an ConcurrentModificationException. */
            if (SimpleHashtable.this.containsKey(lastKey)) {
                savedModificationCount++; // Increase iterators modificationCount so it keeps in sync with HashTable modificationCount
                SimpleHashtable.this.remove(lastKey);
                currentElementNo--;
            }
        }

    }

    /**
     * One TableEntry of SimpleHashTable. It has variable next which points to the next element of this class which is
     * found in the same slot of the table.
     *
     * @param <K> type of table key
     * @param <V> type of table value
     */
    public static class TableEntry<K, V> {

        private final K key;
        private V value;
        private TableEntry<K, V> next;

        public TableEntry(K key, V value, TableEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

    }

}
