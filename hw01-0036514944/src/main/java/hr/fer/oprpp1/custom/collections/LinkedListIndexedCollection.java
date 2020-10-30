package hr.fer.oprpp1.custom.collections;

/**
 * Implementation of linked list-backed collection of objects. Duplicate elements <b>are allowed</b> (each of those
 * element will be held in different list node). Storage of <code>null</code> references <b>is not allowed</b>.
 */
public class LinkedListIndexedCollection extends Collection {

    private int size; // Current size of collection (number of elements actually stored; number of nodes in list).
    private ListNode first; // Reference to the first node of the linked list.
    private ListNode last; // Reference to the last node of the linked list.

    /**
     * Nested class with pointers to previous and next list node and additional reference for value storage.
     */
    private static class ListNode {

        private ListNode previous;
        private ListNode next;
        private final Object value;

        public ListNode(ListNode previous, ListNode next, Object value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }

    }

    /**
     * Default constructor for {@link LinkedListIndexedCollection}.
     */
    public LinkedListIndexedCollection() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * Constructor for {@link LinkedListIndexedCollection} with one parameter.
     *
     * @param other is reference to some other {@link Collection} from which elements are copied into this newly
     *              constructed collection.
     */
    public LinkedListIndexedCollection(Collection other) {
        if (this == other)
            throw new IllegalArgumentException("You can not create new LinkedListIndexedCollection with reference to current LinkedListIndexedCollection.");
        /*
        LinkedListIndexedCollection list = new LinkedListIndexedCollection();
        list = new LinkedListIndexedCollection(list);

        This code would enter infinite loop if not for the exception.
         */

        addAll(other);
    }

    /**
     * @return number of elements in the Collection.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Adds the given object into this collection at the end of collection. Newly added element becomes the element at
     * the biggest index. Implemented with complexity O(1). The method refuses to add <code>null</code> as element by
     * throwing {@link NullPointerException}.
     *
     * @param value added to the Collection.
     */
    @Override
    public void add(Object value) {
        insert(value, size);
    }

    /**
     * Returns the object that is stored in linked list at position index.
     *
     * @param index position of object to return. Valid index is from 0 to (size-1). If index is invalid, the
     *              implementation throws {@link IndexOutOfBoundsException}.
     * @return object at position index.
     */
    public Object get(int index) {
        // Implementation of this method never has complexity greater than n/2+1.
        if (index < 0 || index > (size - 1))
            throw new IndexOutOfBoundsException("Valid index is in from 0 to (size-1).");

        int middleIndex = size / 2;
        ListNode walkerListNode;

        if (index >= middleIndex) { // Then go from last element to first.
            walkerListNode = last;

            for (int currentIndex = size - 1; currentIndex > index; currentIndex--)
                walkerListNode = walkerListNode.previous;
        } else { // Go from first to last element.
            walkerListNode = first;

            for (int currentIndex = 0; currentIndex < index; currentIndex++)
                walkerListNode = walkerListNode.next;
        }

        return walkerListNode.value;
    }

    /**
     * Returns <code>true</code> only if the collection contains given value, as determined by <code>equals</code>
     * method.
     *
     * @param value to find in collection.
     * @return true if collection contains given value, false otherwise.
     */
    @Override
    public boolean contains(Object value) {
        ListNode walkerListNode = first;

        for (int index = 0; index < size && walkerListNode != null; index++) {
            if (walkerListNode.value.equals(value)) return true;
            walkerListNode = walkerListNode.next;
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

        ListNode walkerListNode = first;
        for (int index = 0; index < size && walkerListNode != null; index++) {
            array[index] = walkerListNode.value;
            walkerListNode = walkerListNode.next;
        }

        return array;
    }

    /**
     * Iterates over elements and calls <code>Processor</code>'s method
     * <code>process()</code> with iterated element as argument.
     */
    @Override
    public void forEach(Processor processor) {
        for (ListNode walkerListNode = first; walkerListNode != null; walkerListNode = walkerListNode.next) {
            processor.process(walkerListNode.value);
        }
    }

    /**
     * Removes all elements from the collection. Collection "forgets" about current linked list.
     */
    @Override
    public void clear() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    /**
     * <b>Inserts</b> (does not overwrite) the given value at the given position in linked-list.
     * Elements starting from this position are shifted one position.
     *
     * @param value    to be inserted.
     * @param position value where to insert value. Legal position is from 0 to size. If position is invalid, {@link
     *                 IndexOutOfBoundsException} is thrown.
     */
    public void insert(Object value, int position) {
        if (value == null) throw new NullPointerException("Method add doesn't accept null reference.");
        if (position < 0 || position > size)
            throw new IndexOutOfBoundsException("Position to insert must be between 0 and size of linked-list.");

        ListNode newNode = new ListNode(null, null, value);

        if (first == null) { // If list is empty.
            first = newNode;
            last = newNode;
        } else if (position == 0) { // If we insert at the first position.
            newNode.next = first;
            first.previous = newNode;
            first = newNode;
        } else if (position == size) { // If we insert at the end.
            newNode.previous = last;
            last.next = newNode;
            last = newNode;
        } else { // If we insert in the middle.
            ListNode temp = first; // temp will point to the ListNode before where we insert.
            for (int i = 1; i < position; i++)
                temp = temp.next;

            newNode.next = temp.next;
            temp.next = newNode;
            newNode.previous = temp;
            newNode.next.previous = newNode;
        }

        size++;
    }

    /**
     * Searches the Collection and returns the index of the first occurrence of the given value or -1 if the value is
     * not found. Null is a valid argument.
     *
     * @param value to search the Collection for.
     * @return index of value.
     */
    public int indexOf(Object value) {
        ListNode temp = first;

        for (int index = 0; index < size; index++) {
            if (temp.value.equals(value)) return index;

            temp = temp.next;
        }

        return -1;
    }


    /**
     * @param value to remove from Collection.
     * @return boolean <code>true</code> only if the Collection contains given value as determined by <code>equals</code> method
     *      * and removes one occurrence of it.
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
     * Removes element at specified index from collection. Element that was previously at location (index+1) after this
     * operation is on location index, etc.
     *
     * @param index legal from 0 to (size-1). If not legal index throw {@link IllegalArgumentException}.
     */
    public void remove(int index) {
        if (index < 0 || index > (size - 1)) throw new IllegalArgumentException("Index must be between 0 and size-1.");

        if (index == 0) { // Delete first element.
            if (first == null)
                throw new RuntimeException("Removing element from empty LinkedList.");

            if (first.next == null) {
                last = null;
                first = null;
            } else {
                first.next.previous = null;
                first = first.next;
            }
        } else if (index == size - 1) { // Delete last element.
            if (last == null)
                throw new RuntimeException("Removing element from empty LinkedList.");

            if (first.next == null) {
                first = null;
                last = null;
            } else {
                last.previous.next = null;
                last = last.previous;
            }
        } else { // Delete element in the middle.
            ListNode deleteNode = first;
            for (int stepsToDeleteNode = 0; stepsToDeleteNode < index && deleteNode.next != null; stepsToDeleteNode++)
                deleteNode = deleteNode.next;

            deleteNode.previous.next = deleteNode.next;
            deleteNode.next.previous = deleteNode.previous;
        }

        size--;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListNode current = first;

        while (current != null) {
            sb.append(current.value);
            if (current.next != null)
                sb.append(", ");
            current = current.next;
        }

        return sb.toString();
    }
}
